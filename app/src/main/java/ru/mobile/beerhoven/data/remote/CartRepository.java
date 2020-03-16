package ru.mobile.beerhoven.data.remote;

import static java.util.Objects.requireNonNull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.domain.repository.ICartRepository;
import ru.mobile.beerhoven.utils.Constants;

public class CartRepository implements ICartRepository {
   private final DatabaseReference mFirebaseRef;
   private final List<Product> mProductList;
   private final MutableLiveData<List<Product>> mMutableList;
   private final String mUserPhoneId;

   public CartRepository() {
      this.mFirebaseRef = FirebaseDatabase.getInstance().getReference();
      this.mProductList = new ArrayList<>();
      this.mMutableList = new MutableLiveData<>();
      this.mUserPhoneId = requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();}

   @Override
   public MutableLiveData<List<Product>> getCartMutableList() {
      if (mProductList.size() == 0) {
         readCartList();
      }
      mMutableList.setValue(mProductList);
      return mMutableList;
   }

   // Read cart product list
   private void readCartList() {
      assert mUserPhoneId != null;
      mFirebaseRef.child(Constants.NODE_CART).child(mUserPhoneId).addChildEventListener(new ChildEventListener() {
         @Override
         public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Product order = dataSnapshot.getValue(Product.class);
            assert order != null;
            order.setId(dataSnapshot.getKey());
            if (!mProductList.contains(order)) {
               mProductList.add(order);
            }
            mMutableList.postValue(mProductList);
         }

         @Override
         public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Product order = dataSnapshot.getValue(Product.class);
            assert order != null;
            order.setId(dataSnapshot.getKey());
            if (mProductList.contains(order)) {
               mProductList.set(mProductList.indexOf(order), order);
            }
            mMutableList.postValue(mProductList);
         }

         @Override
         public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            Product order = dataSnapshot.getValue(Product.class);
            assert order != null;
            order.setId(dataSnapshot.getKey());
            mProductList.remove(order);
            mMutableList.postValue(mProductList);
         }

         @Override
         public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

         @Override
         public void onCancelled(@NonNull DatabaseError databaseError) {}
      });
   }

   // Delete cart list item by position
   @Override
   public void onDeleteCartItem(String position) {
      assert mUserPhoneId != null;
      mFirebaseRef.child(Constants.NODE_CART).child(mUserPhoneId).child(position).removeValue();
   }

   // Delete user cart list
   @Override
   public void onDeleteUserCartList() {
      mFirebaseRef.child(Constants.NODE_CART).child(mUserPhoneId).removeValue();
   }
}
