document.addEventListener("DOMContentLoaded", function() {

    loadOrders();

});


async function loadOrders() {

    const token =
        localStorage.getItem("token");

    const customerId =
        localStorage.getItem("customerId");


    if (!token || !customerId) {

        window.location.href = "/login";

        return;
    }


    try {

        const response = await fetch(
            "/orders/customer/" + customerId,
            {
                method: "GET",

                headers: {
                    "Authorization":
                        "Bearer " + token,

                    "Content-Type":
                        "application/json"
                }
            }
        );


        if (response.status === 401 ||
            response.status === 403) {

            localStorage.removeItem("token");
            localStorage.removeItem("customerId");

            alert(
                "Session expired. Please login again."
            );

            window.location.href = "/login";

            return;
        }


        if (!response.ok) {

            throw new Error(
                "Unable to load orders."
            );

        }


        const orders =
            await response.json();


        displayOrders(orders);


    } catch (error) {

        console.error(error);

        showError(
            "Unable to load orders. Please try again."
        );

    }

}


function displayOrders(orders) {

    const loadingMessage =
        document.getElementById("loadingMessage");

    const emptyOrders =
        document.getElementById("emptyOrders");

    const ordersContainer =
        document.getElementById("ordersContainer");

    const ordersList =
        document.getElementById("ordersList");


    loadingMessage.style.display = "none";


    if (!orders ||
        orders.length === 0) {

        emptyOrders.style.display = "block";

        ordersContainer.style.display = "none";

        return;
    }


    emptyOrders.style.display = "none";

    ordersContainer.style.display = "block";

    ordersList.innerHTML = "";


    orders.forEach(function(order) {

        const card =
            document.createElement("div");


        card.className =
            "card shadow-sm mb-4";


        let itemsHtml = "";


        order.items.forEach(function(item) {

            itemsHtml +=

                "<tr>" +

                "<td>" +
                item.productName +
                "</td>" +

                "<td>" +
                item.quantity +
                "</td>" +

                "<td>" +
                "₹ " + item.price +
                "</td>" +

                "<td>" +
                "₹ " + item.subtotal +
                "</td>" +

                "</tr>";

        });


        card.innerHTML =

            "<div class='card-header d-flex justify-content-between align-items-center'>" +

            "<strong>" +
            "Order #" + order.orderId +
            "</strong>" +

            "<span class='badge bg-success'>" +
            order.status +
            "</span>" +

            "</div>" +


            "<div class='card-body'>" +

            "<p>" +
            "<strong>Order Date:</strong> " +
            order.orderDate +
            "</p>" +


            "<div class='table-responsive'>" +

            "<table class='table table-bordered'>" +

            "<thead>" +

            "<tr>" +

            "<th>Product</th>" +

            "<th>Quantity</th>" +

            "<th>Price</th>" +

            "<th>Subtotal</th>" +

            "</tr>" +

            "</thead>" +


            "<tbody>" +

            itemsHtml +

            "</tbody>" +

            "</table>" +

            "</div>" +


            "<div class='text-end'>" +

            "<h5>" +

            "Total: ₹ " +
            order.totalAmount +

            "</h5>" +

            "</div>" +

            "</div>";


        ordersList.appendChild(card);

    });

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