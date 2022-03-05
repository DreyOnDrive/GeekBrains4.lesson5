package lesson5;

import com.github.javafaker.Faker;
import lesson5.api.ProductService;
import lesson5.dto.Product;
import lesson5.utils.RetrofitUtils;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import retrofit2.Response;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;

public class ProductControllerTest {

    static ProductService productService;
    Product product = null;
    Product product2 = null;
    Faker faker = new Faker();
    static int id;

    @BeforeAll
    static void beforeAll() {
        productService = RetrofitUtils.getRetrofit()
                .create(ProductService.class);
    }


    @BeforeEach
    void createProductTest() throws IOException {
        product = new Product()
                .withTitle(faker.food().ingredient())
                .withCategoryTitle("Food")
                .withPrice((int) (Math.random() * 10000));

        System.out.println("createProductTest");
        Response<Product> response = productService.createProduct(product).execute();
        id = response.body().getId();
        System.out.println(response);
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.code(), CoreMatchers.is(201));
        assertThat(response.body().getCategoryTitle(), CoreMatchers.is(product.getCategoryTitle()));
        assertThat(response.body().getTitle(), CoreMatchers.is(product.getTitle()));
        assertThat(response.body().getPrice(), CoreMatchers.is(product.getPrice()));
        assertThat(response.body().getId(), CoreMatchers.notNullValue());
    }


    @Test
    void getProductByIdTest() throws IOException {
        System.out.println("getProductByIdTest");
        Response<Product> response = productService.getProductById(id).execute();
        System.out.println(response);
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.code(), CoreMatchers.is(200));
        assertThat(response.body().getCategoryTitle(), CoreMatchers.is(product.getCategoryTitle()));
        assertThat(response.body().getTitle(), CoreMatchers.is(product.getTitle()));
        assertThat(response.body().getPrice(), CoreMatchers.is(product.getPrice()));
        assertThat(response.body().getId(), CoreMatchers.is(id));
    }


    @Test
    void modifyProductTest() throws IOException {
        product2 = new Product()
                .withTitle(faker.food().ingredient())
                .withCategoryTitle("Food")
                .withPrice((int) (Math.random() * 10000));

        System.out.println("modifyProductTest");
        product2.setId(id);
        Response<Product> response = productService.modifyProduct(product2).execute();
        System.out.println(response);
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.code(), CoreMatchers.is(200));
        assertThat(response.body().getCategoryTitle(), CoreMatchers.is(product2.getCategoryTitle()));
        assertThat(response.body().getTitle(), CoreMatchers.is(product2.getTitle()));
        assertThat(response.body().getPrice(), CoreMatchers.is(product2.getPrice()));
        assertThat(response.body().getId(), CoreMatchers.is(id));
    }


    @Test
    void getProductsTest() throws IOException {
        System.out.println("getProductsTest");
        Response<ResponseBody> response = productService.getProducts().execute();
        System.out.println(response);
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.code(), CoreMatchers.is(500));
    }


    @SneakyThrows
    @AfterAll
    static void tearDown() {
        System.out.println("deleteProduct");
        Response<ResponseBody> response = productService.deleteProduct(id).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.code(), CoreMatchers.is(200));
    }
}
