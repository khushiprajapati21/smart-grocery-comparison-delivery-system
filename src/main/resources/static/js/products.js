function addToCart(productId) {

    const token = localStorage.getItem("token");
    const customerId = localStorage.getItem("customerId");

    // Check login
    if (!token) {

        alert("Please login first.");

        window.location.href = "/login";

        return;
    }

    // Check customer ID
    if (!customerId) {

        alert("Customer information not found. Please login again.");

        localStorage.clear();

        window.location.href = "/login";

        return;
    }

    fetch("/api/cart/add", {

        method: "POST",

        headers: {

            "Content-Type": "application/json",

            "Authorization": "Bearer " + token
        },

        body: JSON.stringify({

            customerId: Number(customerId),

            productId: Number(productId),

            quantity: 1
        })
    })

    .then(async response => {

        if (response.ok) {

            alert("Product added to cart successfully.");

            // Go to cart page
            window.location.href = "/cart";

            return;
        }

        if (response.status === 401 || response.status === 403) {

            alert("Your login session has expired. Please login again.");

            localStorage.clear();

            window.location.href = "/login";

            return;
        }

        const text = await response.text();

        throw new Error(
            text || "Failed to add product to cart."
        );
    })

    .catch(error => {

        console.error("Add To Cart Error:", error);

        alert(error.message);
    });
}

