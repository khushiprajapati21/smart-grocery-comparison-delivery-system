document.addEventListener("DOMContentLoaded", function() {

    loadCart();

});


async function loadCart() {

    const token = localStorage.getItem("token");

    const customerId = localStorage.getItem("customerId");


    if (!token || !customerId) {

        showError("Please login first.");

        return;
    }


    try {

        const response = await fetch(
            `/api/cart/${customerId}`,
            {
                method: "GET",

                headers: {
                    "Authorization": "Bearer " + token,
                    "Content-Type": "application/json"
                }
            }
        );


        if (response.status === 401 ||
            response.status === 403) {

            localStorage.removeItem("token");

            alert("Session expired. Please login again.");

            window.location.href = "/login";

            return;
        }


        if (!response.ok) {

            throw new Error(
                "Unable to load cart."
            );

        }


        const cart = await response.json();


        displayCart(cart);


    } catch (error) {

        console.error(error);

        showError(
            "Unable to load cart. Please try again."
        );

    }

}


function displayCart(cart) {

    const loadingMessage =
        document.getElementById("loadingMessage");

    const emptyCart =
        document.getElementById("emptyCart");

    const cartContainer =
        document.getElementById("cartContainer");

    const cartItems =
        document.getElementById("cartItems");

    const totalAmount =
        document.getElementById("totalAmount");


    loadingMessage.style.display = "none";


    if (!cart.items ||
        cart.items.length === 0) {

        emptyCart.style.display = "block";

        cartContainer.style.display = "none";

        return;
    }


    emptyCart.style.display = "none";

    cartContainer.style.display = "block";


    cartItems.innerHTML = "";


    cart.items.forEach(function(item) {

        const row =
            document.createElement("tr");


        row.innerHTML = `

            <td>
                ${item.productName}
            </td>

            <td>
                ₹ ${item.price}
            </td>

			<td>
			    <div class="d-flex align-items-center gap-2">

			        <button
			            type="button"
			            class="btn btn-outline-secondary"
			            onclick="updateQuantity(${item.productId}, ${item.quantity - 1})">
			            -
			        </button>

			        <span class="fw-bold">
			            ${item.quantity}
			        </span>

			        <button
			            type="button"
			            class="btn btn-outline-secondary"
			            onclick="updateQuantity(${item.productId}, ${item.quantity + 1})">
			            +
			        </button>

			    </div>
			</td>

            <td>
                ₹ ${item.subTotal}
            </td>

            <td>

                <button
                    class="btn btn-danger btn-sm"
                    onclick="removeProduct(${item.productId})">

                    Remove

                </button>

            </td>

        `;


        cartItems.appendChild(row);

    });


    totalAmount.textContent =
        cart.totalAmount;

}


async function updateQuantity(
    productId,
    quantity
) {

    const token =
        localStorage.getItem("token");

    const customerId =
        localStorage.getItem("customerId");


    if (!token || !customerId) {

        alert("Please login first.");

        return;
    }


    if (quantity <= 0) {

        alert("Quantity must be greater than zero.");

        loadCart();

        return;
    }


    try {

        const response = await fetch(
            `/api/cart/update?customerId=${customerId}&productId=${productId}&quantity=${quantity}`,
            {
                method: "PUT",

                headers: {
                    "Authorization": "Bearer " + token
                }
            }
        );


        if (response.status === 401 ||
            response.status === 403) {

            alert("Session expired. Please login again.");

            window.location.href = "/login";

            return;
        }


        if (!response.ok) {

            const errorData =
                await response.json();

            alert(
                errorData.message ||
                "Unable to update quantity."
            );

            loadCart();

            return;
        }


        const cart =
            await response.json();


        displayCart(cart);


    } catch (error) {

        console.error(error);

        alert(
            "Something went wrong while updating quantity."
        );

    }

}


async function removeProduct(productId) {

    const token =
        localStorage.getItem("token");

    const customerId =
        localStorage.getItem("customerId");


    if (!token || !customerId) {

        alert("Please login first.");

        return;
    }


    try {

        const response = await fetch(
            `/api/cart/remove/${customerId}/${productId}`,
            {
                method: "DELETE",

                headers: {
                    "Authorization": "Bearer " + token
                }
            }
        );


        if (response.status === 401 ||
            response.status === 403) {

            alert("Session expired. Please login again.");

            window.location.href = "/login";

            return;
        }


        if (!response.ok) {

            throw new Error(
                "Unable to remove product."
            );

        }


        await loadCart();


    } catch (error) {

        console.error(error);

        alert(
            "Unable to remove product."
        );

    }

}


document
    .getElementById("clearCartBtn")
    .addEventListener("click", clearCart);


async function clearCart() {

    const token =
        localStorage.getItem("token");

    const customerId =
        localStorage.getItem("customerId");


    if (!token || !customerId) {

        alert("Please login first.");

        return;
    }


    try {

        const response = await fetch(
            `/api/cart/clear/${customerId}`,
            {
                method: "DELETE",

                headers: {
                    "Authorization": "Bearer " + token
                }
            }
        );


        if (!response.ok) {

            throw new Error(
                "Unable to clear cart."
            );

        }


        await loadCart();


    } catch (error) {

        console.error(error);

        alert(
            "Unable to clear cart."
        );

    }

}


function showError(message) {

    document
        .getElementById("loadingMessage")
        .style.display = "none";


    const errorMessage =
        document.getElementById("errorMessage");


    errorMessage.textContent = message;

    errorMessage.style.display = "block";

}
const checkoutBtn =
    document.getElementById("checkoutBtn");

if (checkoutBtn) {


    checkoutBtn.addEventListener(
        "click",
        function() {

            const token =
                localStorage.getItem("token");

            const customerId =
                localStorage.getItem("customerId");


            if (!token || !customerId) {

                alert("Please login first.");

                window.location.href = "/login";

                return;
            }


            window.location.href = "/checkout";

        }
    );

}
