package ru.mobile.beerhoven.ui.store.catalog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import ru.mobile.beerhoven.common.FakeContent;
import ru.mobile.beerhoven.domain.model.Product;
import ru.mobile.beerhoven.domain.repository.IProductRepository;
import ru.mobile.beerhoven.presentation.ui.customer.store.catalog.CatalogViewModel;

@RunWith(MockitoJUnitRunner.class)
public class CatalogViewModelTest {
   private CatalogViewModel mViewModel;
   private MutableLiveData mMutableList;

   @Mock
   private IProductRepository mMockRepo;

   @Mock
   private List<String> mMockList;

   @Rule
   public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

   @Before
   public void setUp() {
      mMutableList = new MutableLiveData<>();
      mMockRepo = mock(IProductRepository.class);
      mViewModel = new CatalogViewModel(mMockRepo);
      Mockito.when(mMockRepo.getProductListFromDatabase()).thenReturn(mMutableList);
      setFakeItems();
   }

   private void setFakeItems() {
      List<Product> productList = new ArrayList<>();
      productList.add(FakeContent.product);
      mMutableList.setValue(productList);
   }

   @Test
   public void test_returns_not_assertion_error() {
      String expected = "Not assertion error!";
      when(mMockList.get(0)).thenReturn(expected);
      String actual = mMockList.get(0);
      assertEquals(expected, actual);
      System.out.println(actual);
   }

   @Test
   public void viewModel_getProductListToRepository_should_returns_what_not_null() {
      // Act
      LiveData<List<Product>> result = mViewModel.getProductListToRepository();
      // Assert
      assertNotNull(result);
   }

   @Test
   public void viewModel_getProductListToRepository_should_returns_what_repository_value() {
      // Act
      LiveData<List<Product>> result = mViewModel.getProductListToRepository();
      // Assert
      assertEquals(mMutableList, result);
   }

   @Test
   public void viewModel_getProductListToRepository_should_calls_method() {
      // Act
      mViewModel.getProductListToRepository();
      // Assert
      verify(mMockRepo, times(1)).getProductListFromDatabase();
   }
}
