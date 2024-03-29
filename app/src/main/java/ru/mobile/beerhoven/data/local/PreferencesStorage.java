package ru.mobile.beerhoven.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import ru.mobile.beerhoven.domain.repository.IPreferencesStorage;
import ru.mobile.beerhoven.utils.Constants;

/**
 * Class accessing SharedPreferences, used to work with counter value.
 */
public class PreferencesStorage implements IPreferencesStorage {
   private final SharedPreferences mSettings;
   private final SharedPreferences.Editor mEditor;

   public PreferencesStorage(@NonNull Context context) {
      this.mSettings = context.getSharedPreferences(Constants.SHARED_PREF, Context.MODE_PRIVATE);
      this.mEditor = mSettings.edit();
   }

   @Override
   public void onSaveUserName(String name) {
      mEditor.putString(Constants.KEY_USER_NAME, name).apply();
   }

   @Override
   public String getUserName() {
      return mSettings.getString(Constants.KEY_USER_NAME, "user");
   }

   @Override
   public int onGetNewsCountValue() {
      return mSettings.getInt(Constants.KEY_NEWS_COUNT, 0);
   }

   @Override
   public void onSaveNewsCountValue(int value) {
      mEditor.putInt(Constants.KEY_NEWS_COUNT, value).apply();
   }

   @Override
   public void onDeleteNewsCountValue() {
      mEditor.remove(Constants.KEY_NEWS_COUNT).apply();
   }

   @Override
   public void onSaveCartCountValue(int value) {
      mEditor.putInt(Constants.KEY_CART_COUNT, value).apply();
   }

   @Override
   public int onGetCartCountValue() {
      return mSettings.getInt(Constants.KEY_CART_COUNT, 0);
   }

   @Override
   public void onDeleteCartCountValue() {
      mEditor.remove(Constants.KEY_CART_COUNT).apply();
   }

   @Override
   public int onGetNotificationCountValue() {
      return mSettings.getInt(Constants.KEY_NOTIFICATION_COUNT , 0);
   }

   @Override
   public void onSaveNotificationCountValue(int value) {
      mEditor.putInt(Constants.KEY_NOTIFICATION_COUNT , value).apply();
   }
}
