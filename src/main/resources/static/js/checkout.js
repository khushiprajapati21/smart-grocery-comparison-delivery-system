document.addEventListener("DOMContentLoaded", function () {

    loadCheckout();

});


async function loadCheckout() {

    const token = localStorage.getItem("token");
    const customerId = localStorage.getItem("customerId");

    if (!token || !customerId) {

        window.location.href = "/login";

        return;
    }

    try {

        const response = await fetch(
            "/api/cart/" + customerId,
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
            localStorage.removeItem("customerId");

            alert("Session expired. Please login again.");

            window.location.href = "/login";

            return;
        }


        if (!response.ok) {

            throw new Error("Unable to load cart.");

        }


        const cart = await response.json();

        displayCheckout(cart);


    } catch (error) {

        console.error(error);

        showError(
            "Unable to load checkout. Please try again."
        );

    }

}


function displayCheckout(cart) {

    const loadingMessage =
        document.getElementById("loadingMessage");

    const checkoutContainer =
        document.getElementById("checkoutContainer");

    const emptyCart =
        document.getElementById("emptyCart");

    const checkoutItems =
        document.getElementById("checkoutItems");

    const totalAmount =
        document.getElementById("totalAmount");


    loadingMessage.style.display = "none";


    if (!cart.items ||
        cart.items.length === 0) {

        checkoutContainer.style.display = "none";

        emptyCart.style.display = "block";

        return;
    }


    emptyCart.style.display = "none";

    checkoutContainer.style.display = "block";


    checkoutItems.innerHTML = "";


    cart.items.forEach(function (item) {

        const row =
            document.createElement("tr");


        row.innerHTML =

            "<td>" +
                item.productName +
            "</td>" +

            "<td>" +
                "₹ " + item.price +
            "</td>" +

            "<td>" +
                item.quantity +
            "</td>" +

            "<td>" +
                "₹ " + item.subTotal +
            "</td>";


        checkoutItems.appendChild(row);

    });


    totalAmount.textContent =
        cart.totalAmount;

}


const placeOrderBtn =
    document.getElementById("placeOrderBtn");


if (placeOrderBtn) {

    placeOrderBtn.addEventListener(
        "click",
        placeOrder
    );

}


async function placeOrder() {

    const token =
        localStorage.getItem("token");

    const customerId =
        localStorage.getItem("customerId");


    if (!token || !customerId) {

        alert("Please login first.");

        window.location.href = "/login";

        return;
    }


    const button =
        document.getElementById("placeOrderBtn");


    button.disabled = true;

    button.textContent =
        "Placing Order...";


    try {

        const response = await fetch(
            "/orders/place",
            {
                method: "POST",

                headers: {
                    "Authorization":
                        "Bearer " + token,

                    "Content-Type":
                        "application/json"
                },

                body: JSON.stringify({
                    customerId:
                        Number(customerId)
                })
            }
        );


        if (response.status === 401 ||
            response.status === 403) {

            alert(
                "Session expired. Please login again."
            );

            window.location.href = "/login";

            return;
        }


        if (!response.ok) {

            let message =
                "Unable to place order.";


            try {

                const errorData =
                    await response.json();

                message =
                    errorData.message || message;

            } catch (e) {

                // Ignore JSON parsing error

            }


            alert(message);

            button.disabled = false;

            button.textContent =
                "Place Order";

            return;
        }


        const order =
            await response.json();


        alert(
            "Order placed successfully!"
        );


        window.location.href =
            "/orders/success?orderId=" +
            order.orderId;


    } catch (error) {

        console.error(error);

        alert(
            "Something went wrong while placing the order."
        );


        button.disabled = false;

        button.textContent =
            "Place Order";

    }

}


function showError(message) {

    document
        .getElementById("loadingMessage")
        .style.display = "none";


    const errorMessage =
        document.getElementById("errorMessage");


    errorMessage.textContent =
        message;


    errorMessage.style.display =
        "block";

}