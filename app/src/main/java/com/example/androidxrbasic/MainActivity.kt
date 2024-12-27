package com.example.androidxrbasic

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.xr.compose.platform.LocalSession
import androidx.xr.compose.platform.LocalSpatialCapabilities
import androidx.xr.compose.spatial.EdgeOffset
import androidx.xr.compose.spatial.Orbiter
import androidx.xr.compose.spatial.OrbiterEdge
import androidx.xr.compose.spatial.Subspace
import androidx.xr.compose.subspace.SpatialPanel
import androidx.xr.compose.subspace.layout.SpatialRoundedCornerShape
import androidx.xr.compose.subspace.layout.SubspaceModifier
import androidx.xr.compose.subspace.layout.height
import androidx.xr.compose.subspace.layout.movable
import androidx.xr.compose.subspace.layout.resizable
import androidx.xr.compose.subspace.layout.width
import com.example.androidxrbasic.ui.theme.AndroidXRBasicTheme
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.ui.unit.Dp

class MainActivity : ComponentActivity() {

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AndroidXRBasicTheme {
                val session = LocalSession.current
                // Cihazın spatial UI özelliklerini kontrol et
                if (LocalSpatialCapabilities.current.isSpatialUiEnabled) {
                    // Spatial UI destekleniyorsa, Subspace içinde MySpatialContent'i göster
                    Subspace {
                        MySpatialContent(onRequestHomeSpaceMode = { session?.requestHomeSpaceMode() })
                    }
                } else {
                    // Spatial UI desteklenmiyorsa, normal 2D içeriği göster
                    My2DContent(onRequestFullSpaceMode = { session?.requestFullSpaceMode() })
                }
            }
        }
    }
}

/**
 * Slayt animasyonlarını yöneten yardımcı fonksiyon
 * @param targetState Hedef durum indeksi
 * @param initialState Başlangıç durum indeksi
 * @param durationMillis Animasyon süresi (milisaniye)
 */
private fun getSlideAnimation(
    targetState: Int,
    initialState: Int,
    durationMillis: Int = 500
) = if (targetState > initialState) {
    // İleri yönde animasyon (sağdan sola)
    (slideInHorizontally(
        initialOffsetX = { fullWidth -> fullWidth },
        animationSpec = tween(durationMillis = durationMillis)
    ) + fadeIn(animationSpec = tween(durationMillis))).togetherWith(
        slideOutHorizontally(
            targetOffsetX = { fullWidth -> -fullWidth },
            animationSpec = tween(durationMillis = durationMillis)
        ) + fadeOut(animationSpec = tween(durationMillis))
    )
} else {
    // Geri yönde animasyon (soldan sağa)
    (slideInHorizontally(
        initialOffsetX = { fullWidth -> -fullWidth },
        animationSpec = tween(durationMillis = durationMillis)
    ) + fadeIn(animationSpec = tween(durationMillis))).togetherWith(
        slideOutHorizontally(
            targetOffsetX = { fullWidth -> fullWidth },
            animationSpec = tween(durationMillis = durationMillis)
        ) + fadeOut(animationSpec = tween(durationMillis))
    )
}

/**
 * F1 pisti içeriğini gösteren composable fonksiyon
 * @param track Gösterilecek F1 pisti
 * @param imageHeight Görüntü yüksekliği
 */
@Composable
private fun TrackContent(
    track: F1Track,
    imageHeight: Dp
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Pist görselini göster
        Image(
            painter = painterResource(id = track.imageRes),
            contentDescription = track.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight)
        )

        // Pist adını göster
        Text(
            text = track.name,
            modifier = Modifier.padding(16.dp),
            fontSize = 24.sp
        )
    }
}

/**
 * Pist indeksini güncelleyen yardımcı fonksiyon
 * @param currentIndex Mevcut pist indeksi
 * @param forward İleri yönde mi (true) yoksa geri yönde mi (false) hareket edileceği
 * @param totalTracks Toplam pist sayısı
 */
private fun updateTrackIndex(currentIndex: Int, forward: Boolean, totalTracks: Int): Int =
    when {
        forward -> (currentIndex + 1) % totalTracks // İleri yönde döngüsel hareket
        currentIndex == 0 -> totalTracks - 1 // Başlangıçtayken sona git
        else -> currentIndex - 1 // Geri git
    }

/**
 * Spatial UI modunda içeriği gösteren ana composable
 * @param onRequestHomeSpaceMode Home Space moduna geçiş isteği callback'i
 */
@Composable
fun MySpatialContent(onRequestHomeSpaceMode: () -> Unit) {
    // F1 pistlerini ve mevcut pist indeksini al
    val f1Tracks = F1TrackRepository.f1Tracks
    var currentTrackIndex by rememberF1TrackIndex()

    // Spatial panel oluştur
    SpatialPanel(SubspaceModifier.width(1280.dp).height(800.dp).resizable().movable()) {
        Surface {
            // Animasyonlu içerik geçişi
            AnimatedContent(
                targetState = currentTrackIndex,
                transitionSpec = { getSlideAnimation(targetState, initialState) }
            ) { targetIndex ->
                TrackContent(
                    track = f1Tracks[targetIndex],
                    imageHeight = 600.dp
                )
            }
        }

        // Üst kısımda Home Space modu düğmesi
        Orbiter(
            position = OrbiterEdge.Top,
            offset = EdgeOffset.inner(offset = 20.dp),
            alignment = Alignment.End,
            shape = SpatialRoundedCornerShape(CornerSize(28.dp))
        ) {
            HomeSpaceModeIconButton(
                onClick = onRequestHomeSpaceMode,
                modifier = Modifier.size(56.dp)
            )
        }

        // Alt sol kısımda geri düğmesi
        Orbiter(
            position = OrbiterEdge.Bottom,
            offset = EdgeOffset.inner(offset = 20.dp),
            alignment = Alignment.Start,
            shape = SpatialRoundedCornerShape(CornerSize(28.dp))
        ) {
            BackIconButton(
                onClick = { currentTrackIndex = updateTrackIndex(currentTrackIndex, false, f1Tracks.size) },
                modifier = Modifier.size(56.dp)
            )
        }

        // Alt sağ kısımda ileri düğmesi
        Orbiter(
            position = OrbiterEdge.Bottom,
            offset = EdgeOffset.inner(offset = 20.dp),
            alignment = Alignment.End,
            shape = SpatialRoundedCornerShape(CornerSize(28.dp))
        ) {
            NextIconButton(
                onClick = { currentTrackIndex = updateTrackIndex(currentTrackIndex, true, f1Tracks.size) },
                modifier = Modifier.size(56.dp)
            )
        }
    }
}

/**
 * 2D modunda içeriği gösteren ana composable
 * @param onRequestFullSpaceMode Full Space moduna geçiş isteği callback'i
 */
@Composable
fun My2DContent(onRequestFullSpaceMode: () -> Unit) {
    // F1 pistlerini ve mevcut pist indeksini al
    val f1Tracks = F1TrackRepository.f1Tracks
    var currentTrackIndex by rememberF1TrackIndex()

    // Ana container
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Animasyonlu içerik geçişi
        AnimatedContent(
            targetState = currentTrackIndex,
            transitionSpec = { getSlideAnimation(targetState, initialState) }
        ) { targetIndex ->
            TrackContent(
                track = f1Tracks[targetIndex],
                imageHeight = 300.dp
            )
        }

        // Üst sağ köşede Full Space modu düğmesi
        IconButton(
            onClick = onRequestFullSpaceMode,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(56.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_full_space_mode_switch),
                contentDescription = stringResource(R.string.switch_to_full_space_mode)
            )
        }

        // Alt sol köşede geri düğmesi
        IconButton(
            onClick = { currentTrackIndex = updateTrackIndex(currentTrackIndex, false, f1Tracks.size) },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
                .size(56.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }

        // Alt sağ köşede ileri düğmesi
        IconButton(
            onClick = { currentTrackIndex = updateTrackIndex(currentTrackIndex, true, f1Tracks.size) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(56.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Next"
            )
        }
    }
}

@Composable
fun FullSpaceModeIconButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            painter = painterResource(id = R.drawable.ic_full_space_mode_switch),
            contentDescription = stringResource(R.string.switch_to_full_space_mode)
        )
    }
}

@Composable
fun HomeSpaceModeIconButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    FilledTonalIconButton(onClick = onClick, modifier = modifier) {
        Icon(
            painter = painterResource(id = R.drawable.ic_home_space_mode_switch),
            contentDescription = stringResource(R.string.switch_to_home_space_mode)
        )
    }
}

@Composable
fun BackIconButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    FilledTonalIconButton(onClick = onClick, modifier = modifier) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = ""
        )
    }
}

@Composable
fun NextIconButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    FilledTonalIconButton(onClick = onClick, modifier = modifier) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = ""
        )
    }
}

@PreviewLightDark
@Composable
fun My2dContentPreview() {
    AndroidXRBasicTheme {
        My2DContent(onRequestFullSpaceMode = {})
    }
}

@Preview(showBackground = true)
@Composable
fun FullSpaceModeButtonPreview() {
    AndroidXRBasicTheme {
        FullSpaceModeIconButton(onClick = {})
    }
}

@PreviewLightDark
@Composable
fun HomeSpaceModeButtonPreview() {
    AndroidXRBasicTheme {
        HomeSpaceModeIconButton(onClick = {})
    }
}