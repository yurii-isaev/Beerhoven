package ru.mobile.beerhoven.data.repository;

import static java.util.Objects.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.domain.repository.ICatalogRepository;
import ru.mobile.beerhoven.utils.Constants;
import ru.mobile.beerhoven.utils.HashMapRepository;

public class CatalogRepository implements ICatalogRepository {
   private final List<Product> mDataList;
   private final MutableLiveData<List<Product>> mMutableList;
   private final MutableLiveData<String> mValue;
   private final String UID;
   private final DatabaseReference mInstanceFirebase;

   public CatalogRepository() {
      this.mDataList = new ArrayList<>();
      this.mMutableList = new MutableLiveData<>();
      this.mValue = new MutableLiveData<>();
      this.UID = requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
      this.mInstanceFirebase = FirebaseDatabase.getInstance().getReference();
   }

   // Read store catalog
   @Override
   public MutableLiveData<List<Product>> readProductList() {
      if (mDataList.size() == 0) {
         readCatalogList();
      }
      mMutableList.setValue(mDataList);
      return mMutableList;
   }

   private void readCatalogList() {
      mInstanceFirebase.child(Constants.NODE_ITEMS).addChildEventListener(new ChildEventListener() {
         @Override
         public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Product product = snapshot.getValue(Product.class);
            assert product != null;
            product.setId(snapshot.getKey());

            if (!mDataList.contains(product)) {
               mDataList.add(product);
            }
            mMutableList.postValue(mDataList);
         }

         @Override
         public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            Product product = snapshot.getValue(Product.class);
            assert product != null;
            product.setId(snapshot.getKey());
            if (mDataList.contains(product)) {
               mDataList.set(mDataList.indexOf(product), product);
            }
            mMutableList.postValue(mDataList);
         }

         @Override
         public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            Product product = snapshot.getValue(Product.class);
            assert product != null;
            product.setId(snapshot.getKey());
            if (mDataList.contains(product)) {
               mDataList.remove(product);
            }
            mMutableList.postValue(mDataList);
         }

         @Override
         public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

         @Override
         public void onCancelled(@NonNull DatabaseError error) {}
      });
   }

   // Create store catalog item
   @Override
   public MutableLiveData<String> addProductToCart() {
      addCatalogItem();
      mValue.setValue(null);
      return mValue;
   }

   private void addCatalogItem() {
      HashMap<String, String> id = HashMapRepository.idMap;
      HashMap<String, String> catalog = HashMapRepository.catalogMap;
      HashMap<String, Double> price = HashMapRepository.priceMap;

      Product post = new Product();
      post.setName(catalog.get("name"));
      post.setCountry(catalog.get("country"));
      post.setManufacture(catalog.get("manufacture"));
      post.setStyle(catalog.get("style"));
      post.setFortress(catalog.get("fortress"));
      post.setDensity(catalog.get("density"));
      post.setDescription(catalog.get("description"));
      post.setDate(catalog.get("ru/mobile/beerhoven/data"));
      post.setTime(catalog.get("time"));
      post.setUrl(catalog.get("url"));
      post.setQuantity(catalog.get("quantity"));
      post.setPrice(price.get("catalog_price"));
      post.setTotal(price.get("catalog_total"));

      assert UID != null;
      mInstanceFirebase.child(Constants.NODE_CART).child(UID)
          .child(requireNonNull(id.get("catalog_id"))).setValue(post);
   }

   // Delete store catalog item
   @Override
   public MutableLiveData<String> deleteProductFromCart() {
      deleteCatalogItem();
      mValue.setValue(null);
      return mValue;
   }

   private void deleteCatalogItem() {
      HashMap<String, String> pid = HashMapRepository.pushMap;
      mInstanceFirebase.child(Constants.NODE_ITEMS).child(requireNonNull(pid.get("item_id"))).removeValue();
      FirebaseStorage.getInstance().getReferenceFromUrl(requireNonNull(pid.get("image"))).delete();
   }
}
