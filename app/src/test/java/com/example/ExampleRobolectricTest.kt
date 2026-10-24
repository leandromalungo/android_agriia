package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import kotlinx.coroutines.flow.first
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("agriia", appName)
  }

  @Test
  fun `verify user preferences repository profile update`() = kotlinx.coroutines.runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repo = com.agriia.app.core.datastore.UserPreferencesRepository(context)
    repo.updateUserName("Carlos Silva")
    repo.updateUserEmail("carlos@fazenda.com")
    repo.updateFarmName("Fazenda Boa Esperança")
    repo.updateUserPhotoUrl("https://example.com/photo.png")

    val name = repo.userNameFlow.first()
    val email = repo.userEmailFlow.first()
    val farm = repo.farmNameFlow.first()
    val photo = repo.userPhotoUrlFlow.first()

    assertEquals("Carlos Silva", name)
    assertEquals("carlos@fazenda.com", email)
    assertEquals("Fazenda Boa Esperança", farm)
    assertEquals("https://example.com/photo.png", photo)
  }
}
