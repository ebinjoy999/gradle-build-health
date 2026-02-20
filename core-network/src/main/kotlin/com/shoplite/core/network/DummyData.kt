package com.shoplite.core.network

import com.shoplite.core.model.Category
import com.shoplite.core.model.Product

/**
 * Dummy data for the ShopLite app.
 * 
 * This simulates data that would come from a backend API.
 */
internal object DummyData {

    val categories = listOf(
        Category(id = 1, name = "Electronics", description = "Gadgets and devices"),
        Category(id = 2, name = "Clothing", description = "Fashion and apparel"),
        Category(id = 3, name = "Home & Garden", description = "Home essentials"),
        Category(id = 4, name = "Sports", description = "Sports equipment"),
        Category(id = 5, name = "Books", description = "Books and media")
    )

    val products = listOf(
        // Electronics
        Product(
            id = 1,
            name = "Wireless Headphones",
            price = 79.99,
            category = categories[0],
            description = "High-quality wireless headphones with noise cancellation"
        ),
        Product(
            id = 2,
            name = "Smartphone Stand",
            price = 24.99,
            category = categories[0],
            description = "Adjustable aluminum stand for phones and tablets"
        ),
        Product(
            id = 3,
            name = "USB-C Hub",
            price = 49.99,
            category = categories[0],
            description = "7-in-1 USB-C hub with HDMI and card reader"
        ),
        Product(
            id = 4,
            name = "Portable Charger",
            price = 39.99,
            category = categories[0],
            description = "20000mAh portable power bank"
        ),
        
        // Clothing
        Product(
            id = 5,
            name = "Cotton T-Shirt",
            price = 19.99,
            category = categories[1],
            description = "Comfortable 100% cotton t-shirt"
        ),
        Product(
            id = 6,
            name = "Denim Jeans",
            price = 59.99,
            category = categories[1],
            description = "Classic fit denim jeans"
        ),
        Product(
            id = 7,
            name = "Running Shoes",
            price = 89.99,
            category = categories[1],
            description = "Lightweight running shoes with cushioning"
        ),
        
        // Home & Garden
        Product(
            id = 8,
            name = "LED Desk Lamp",
            price = 34.99,
            category = categories[2],
            description = "Adjustable LED lamp with multiple brightness levels"
        ),
        Product(
            id = 9,
            name = "Plant Pot Set",
            price = 29.99,
            category = categories[2],
            description = "Set of 3 ceramic plant pots"
        ),
        Product(
            id = 10,
            name = "Throw Blanket",
            price = 44.99,
            category = categories[2],
            description = "Soft fleece throw blanket"
        ),
        
        // Sports
        Product(
            id = 11,
            name = "Yoga Mat",
            price = 29.99,
            category = categories[3],
            description = "Non-slip yoga mat with carrying strap"
        ),
        Product(
            id = 12,
            name = "Resistance Bands",
            price = 14.99,
            category = categories[3],
            description = "Set of 5 resistance bands"
        ),
        Product(
            id = 13,
            name = "Water Bottle",
            price = 19.99,
            category = categories[3],
            description = "32oz insulated water bottle"
        ),
        
        // Books
        Product(
            id = 14,
            name = "Kotlin Programming",
            price = 49.99,
            category = categories[4],
            description = "Complete guide to Kotlin programming"
        ),
        Product(
            id = 15,
            name = "Android Development",
            price = 54.99,
            category = categories[4],
            description = "Modern Android app development with Compose"
        )
    )
}
