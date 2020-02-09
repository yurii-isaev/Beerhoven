package ru.mobile.beerhoven.data.repository;

import static java.util.Objects.*;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.utils.Constants;
import ru.mobile.beerhoven.utils.HashMapRepository;

public class DetailsRepository {
   private final MutableLiveData<String> mLiveData;
   private final DatabaseReference mInstanceFirebase;
   private final String UID;
   private String data = null;

   public DetailsRepository() {
      this.mLiveData = new MutableLiveData<>();
      this.mInstanceFirebase = FirebaseDatabase.getInstance().getReference();
      this.UID = requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
   }

   // Create an instance of a product to the cart
   public MutableLiveData<String> createProductToCart() {
      if (data == null) {
         addPostToCartList();
      }
      mLiveData.setValue(data);
      return mLiveData;
   }

   private void addPostToCartList() {
      HashMap<String, String> id = HashMapRepository.idMap;
      HashMap<String, String> map = HashMapRepository.detailsMap;
      HashMap<String, Double> price = HashMapRepository.priceMap;

      Product post = new Product();
      post.setCountry(map.get("country"));
      post.setDate(map.get("data"));
      post.setDensity(map.get("density"));
      post.setDescription(map.get("description"));
      post.setFortress(map.get("fortress"));
      post.setManufacture(map.get("manufacture"));
      post.setName(map.get("name"));
      post.setPrice(price.get("details_price"));
      post.setQuantity(map.get("quantity"));
      post.setStyle(map.get("style"));
      post.setTime(map.get("time"));
      post.setTotal(price.get("details_total"));
      post.setUrl(map.get("url"));

      assert UID != null;
      mInstanceFirebase.child(Constants.NODE_CART).child(UID)
          .child(requireNonNull(id.get("details_id")))
          .setValue(post);
   }
}
