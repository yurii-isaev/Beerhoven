package ru.mobile.beerhoven.presentation.ui.user.orders;

import static java.util.Objects.requireNonNull;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.List;

import ru.mobile.beerhoven.data.remote.OrderRepository;
import ru.mobile.beerhoven.databinding.ItemOrderBinding;
import ru.mobile.beerhoven.domain.model.Order;

public class OrderListAdapter extends Adapter<OrderListAdapter.OrderViewHolder> {
   private final List<Order> mOrderList;

   public OrderListAdapter(@NonNull List<Order> list) {
      this.mOrderList = list;
   }

   @NonNull
   @Override
   public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      ItemOrderBinding binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
      return new OrderViewHolder(binding);
   }

   @SuppressLint("SetTextI18n")
   @Override
   public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
      OrderListViewModel viewModel = new OrderListViewModel(new OrderRepository());
      Order order = mOrderList.get(position);
      String orderId = order.getId();
      String userId = order.getPhone();

      holder.binding.orderAddress.setText(order.getAddress());
      holder.binding.orderDate.setText(order.getDate());
      holder.binding.orderName.setText(order.getName());
      holder.binding.orderPhone.setText(order.getPhone());
      holder.binding.orderTime.setText(order.getTime());
      holder.binding.orderTotal.setText(order.getTotal() + " руб.");
      holder.binding.imgOrderDelete.setVisibility(View.INVISIBLE);
      holder.binding.imgOrderDelete.setOnClickListener(v -> {
         viewModel.onDeleteOrderByIdToRepository(userId);
      });
      holder.itemView.setOnClickListener(v -> {
         NavDirections action = OrderListFragmentDirections.actionNavOrdersToNavOrderDetails()
             .setUserId(requireNonNull(userId))
             .setOrderKey(requireNonNull(orderId));
         Navigation.findNavController(v).navigate(action);
      });
   }

   @Override
   public int getItemCount() {
      return mOrderList.size();
   }

   public static class OrderViewHolder extends ViewHolder {
      ItemOrderBinding binding;

      public OrderViewHolder(@NonNull ItemOrderBinding recyclerBinding) {
         super(recyclerBinding.getRoot());
         this.binding = recyclerBinding;
      }
   }
}