package com.sametozbalkan.dummyproject.product

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.sametozbalkan.dummyproject.modal.Product
import com.sametozbalkan.dummyproject.service.RetrofitInstance
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(navBackStackEntry: NavBackStackEntry, navController: NavController) {
    val productId = navBackStackEntry.arguments?.getString("id")?.toIntOrNull() ?: return
    var product by remember { mutableStateOf<Product?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(productId) {
        coroutineScope.launch {
            product = RetrofitInstance.api.getProductDetails(productId)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Product Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Geri"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        product?.let { productDetails ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Image(
                        painter = rememberAsyncImagePainter(productDetails.thumbnail),
                        contentDescription = productDetails.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }

                item {
                    Text(
                        text = productDetails.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = productDetails.description,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Justify
                    )
                }

                item {
                    Text(
                        "Price: $${productDetails.price}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        "Discount: ${productDetails.discountPercentage}%",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text("Stock: ${productDetails.stock} units (${productDetails.availabilityStatus})")
                }

                item {
                    Text("Brand: ${productDetails.brand}")
                    Text("SKU: ${productDetails.sku}")
                    Text("Weight: ${productDetails.weight} kg")
                    Text("Dimensions: ${productDetails.dimensions.width} x ${productDetails.dimensions.height} x ${productDetails.dimensions.depth} cm")
                }

                item {
                    Text("Warranty: ${productDetails.warrantyInformation}")
                    Text("Shipping: ${productDetails.shippingInformation}")
                }

                item {
                    Text("Tags: ${productDetails.tags.joinToString(", ")}")
                }

                item {
                    Text("Return Policy: ${productDetails.returnPolicy}")
                }

                item {
                    Text(
                        text = "Reviews:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                items(productDetails.reviews) { review ->
                    ReviewCard(review)
                }

                item {
                    Text("Barcode: ${productDetails.meta.barcode}")
                    Image(
                        painter = rememberAsyncImagePainter(productDetails.meta.qrCode),
                        contentDescription = "QR Code",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                    )
                }
            }
        } ?: Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun ReviewCard(review: com.sametozbalkan.dummyproject.modal.Review) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "${review.reviewerName} (${review.rating}/5)",
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "\"${review.comment}\"",
            style = MaterialTheme.typography.bodySmall,
            overflow = TextOverflow.Ellipsis,
            maxLines = 3
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Date: ${review.date}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
