package com.music.bitchord.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.music.bitchord.R
import com.music.bitchord.data.listentogether.ListenTogether
import com.music.bitchord.data.model.Account
import com.music.bitchord.data.settings.AppSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

/**
 * Settings → Profile: the photo shown in the top bar and the name friends see
 * in Listen Together. The photo is the user's own pick, kept in app storage;
 * without one the signed-in account's photo is used, as before.
 */
@Composable
internal fun ProfileSettingsCard(account: Account?) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val customPhoto by AppSettings.profilePhoto.collectAsStateWithLifecycle()
    var name by remember { mutableStateOf(ListenTogether.nickname().ifBlank { account?.name.orEmpty() }) }
    val failed = stringResource(R.string.profile_photo_failed)

    val picker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        scope.launch {
            val path = withContext(Dispatchers.IO) { runCatching { saveProfilePhoto(context, uri) }.getOrNull() }
            if (path != null) AppSettings.setProfilePhoto(path)
            else Toast.makeText(context, failed, Toast.LENGTH_SHORT).show()
        }
    }
    val pickPhoto = {
        picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val photo: Any? = customPhoto?.let(::File) ?: account?.thumbnailUrl
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
                .clickable(onClick = pickPhoto),
            contentAlignment = Alignment.Center,
        ) {
            if (photo != null) {
                AsyncImage(
                    model = photo,
                    contentDescription = stringResource(R.string.profile_change_photo),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(72.dp),
                )
            } else {
                Icon(
                    Icons.Rounded.Person,
                    contentDescription = stringResource(R.string.profile_change_photo),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(34.dp),
                )
            }
        }
        Spacer(Modifier.width(16.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it.take(80)
                    ListenTogether.setNickname(name)
                },
                label = { Text(stringResource(R.string.profile_name)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                modifier = Modifier.fillMaxWidth(),
            )
            Row {
                TextButton(onClick = pickPhoto) { Text(stringResource(R.string.profile_change_photo)) }
                if (customPhoto != null) {
                    TextButton(onClick = {
                        customPhoto?.let { File(it).delete() }
                        AppSettings.setProfilePhoto(null)
                    }) { Text(stringResource(R.string.profile_remove_photo)) }
                }
            }
        }
    }
}

/** Decodes the picked image, scales it to a 512px avatar and saves it; returns its path. */
private fun saveProfilePhoto(context: Context, uri: Uri): String {
    val resolver = context.contentResolver
    val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    resolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, bounds) }
    var sample = 1
    while (maxOf(bounds.outWidth, bounds.outHeight) / (sample * 2) >= MAX_SIDE) sample *= 2
    val decoded = resolver.openInputStream(uri)?.use {
        BitmapFactory.decodeStream(it, null, BitmapFactory.Options().apply { inSampleSize = sample })
    } ?: error("unreadable image")
    val longest = maxOf(decoded.width, decoded.height)
    val bitmap = if (longest > MAX_SIDE) {
        val k = MAX_SIDE.toFloat() / longest
        Bitmap.createScaledBitmap(decoded, (decoded.width * k).toInt().coerceAtLeast(1), (decoded.height * k).toInt().coerceAtLeast(1), true)
    } else {
        decoded
    }
    val dir = File(context.filesDir, "profile").apply { mkdirs() }
    // A fresh name each time, so the image loader never serves the old photo from cache.
    val file = File(dir, "photo_${System.currentTimeMillis()}.jpg")
    file.outputStream().use { bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it) }
    dir.listFiles()?.filter { it != file }?.forEach { it.delete() }
    return file.absolutePath
}

private const val MAX_SIDE = 512
