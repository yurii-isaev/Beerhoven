package ru.mobile.beerhoven.presentation.authentication;

import static androidx.navigation.Navigation.findNavController;
import static java.util.Objects.requireNonNull;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.data.remote.AuthRepository;
import ru.mobile.beerhoven.presentation.activity.MainActivity;

public class SplashFragment extends Fragment {
   private String mUserId;

   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      return inflater.inflate(R.layout.fragment_splash, container, false);
   }

   @Override
   public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      AuthViewModel mViewModel = new AuthViewModel(new AuthRepository());

      mViewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> mUserId = user.getId());

      new Handler().postDelayed(() -> {
         if (!mUserId.equals("null")) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            requireActivity().finish();
         } else {
            if ((requireNonNull(findNavController(view).getCurrentDestination()).getId()) == R.id.nav_splash) {
               NavDirections action = SplashFragmentDirections.actionNavSplashToNavReg();
               findNavController(view).navigate(action);
            }
         }
      }, 3000);
   }
}
