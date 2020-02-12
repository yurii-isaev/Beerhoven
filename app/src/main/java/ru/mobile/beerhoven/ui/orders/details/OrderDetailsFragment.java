package ru.mobile.beerhoven.ui.orders.details;

import static java.util.Objects.*;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.data.remote.OrderDetailsRepository;
import ru.mobile.beerhoven.domain.model.Product;

public class OrderDetailsFragment extends Fragment {
   private RecyclerView mRecyclerView;
   private OrderDetailsAdapter mOrderDetailsAdapter;
   private OrderDetailsViewModel mOrderDetailsViewModel;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      if (getArguments() != null) {
         OrderDetailsFragmentArgs argsOrder = OrderDetailsFragmentArgs.fromBundle(getArguments());
         String mOrderID = argsOrder.getID();
         String mPushID = argsOrder.getPushID();
         // OrderDetailsRepository.getInstance().stateMap.put("push_id", mPushID);
      }
   }

   @SuppressLint("NotifyDataSetChanged")
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      mOrderDetailsViewModel = new OrderDetailsViewModel(new OrderDetailsRepository());

      View view = inflater.inflate(R.layout.fragment_order_details, container, false);
      mRecyclerView = view.findViewById(R.id.recycler_view_cart);

      mOrderDetailsViewModel.initOrderDetailsList();

      // Get order details list and order adapter aka observe
      mOrderDetailsViewModel.getOrderDetailsList().observe(getViewLifecycleOwner(),
          (List<Product> list) -> mOrderDetailsAdapter.notifyDataSetChanged());

      initRecyclerView();
      return view;
   }

   @SuppressLint("NotifyDataSetChanged")
   private void initRecyclerView() {
      mRecyclerView.setHasFixedSize(true);
      mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
      mOrderDetailsAdapter = new OrderDetailsAdapter(requireNonNull(mOrderDetailsViewModel.getOrderDetailsList().getValue()));
      mRecyclerView.setAdapter(mOrderDetailsAdapter);
      mOrderDetailsAdapter.notifyDataSetChanged();
   }
}
