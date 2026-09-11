document.addEventListener("DOMContentLoaded", function () {

    loadOrders();

});


async function loadOrders() {

    const token =
        localStorage.getItem("token");


    if (!token) {

        window.location.href = "/login";

        return;
    }


    try {

        const response = await fetch(
            "/orders/all",
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

            alert(
                "You are not authorized to access orders."
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

    const ordersContainer =
        document.getElementById("ordersContainer");

    const emptyOrders =
        document.getElementById("emptyOrders");

    const tableBody =
        document.getElementById("ordersTableBody");


    loadingMessage.style.display = "none";


    if (!orders ||
        orders.length === 0) {

        emptyOrders.style.display = "block";

        ordersContainer.style.display = "none";

        return;
    }


    emptyOrders.style.display = "none";

    ordersContainer.style.display = "block";


    tableBody.innerHTML = "";


    orders.forEach(function (order) {

        const row =
            document.createElement("tr");


        row.innerHTML =

            "<td>" +
                order.orderId +
            "</td>" +

            "<td>" +
                order.customerName +
            "</td>" +

            "<td>" +
                "₹ " + order.totalAmount +
            "</td>" +

            "<td>" +
                order.orderDate +
            "</td>" +

            "<td>" +

                "<select " +
                    "class='form-select' " +
                    "id='status-" +
                    order.orderId +
                    "'>" +

                    "<option value='PENDING'>PENDING</option>" +

                    "<option value='CONFIRMED'>CONFIRMED</option>" +

                    "<option value='PREPARING'>PREPARING</option>" +

                    "<option value='READY_FOR_PICKUP'>READY_FOR_PICKUP</option>" +

                    "<option value='OUT_FOR_DELIVERY'>OUT_FOR_DELIVERY</option>" +

                    "<option value='DELIVERED'>DELIVERED</option>" +

                    "<option value='CANCELLED'>CANCELLED</option>" +

                "</select>" +

            "</td>" +

            "<td>" +

                "<button " +
                    "class='btn btn-success btn-sm' " +
                    "onclick='updateStatus(" +
                    order.orderId +
                    ")'>" +

                    "Update" +

                "</button>" +

            "</td>";


        tableBody.appendChild(row);


        const statusSelect =
            document.getElementById(
                "status-" + order.orderId
            );


        statusSelect.value =
            order.status;

    });

}


async function updateStatus(orderId) {

    const token =
        localStorage.getItem("token");


    const statusSelect =
        document.getElementById(
            "status-" + orderId
        );


    const newStatus =
        statusSelect.value;


    try {

        const response = await fetch(
            "/orders/" +
            orderId +
            "/status",
            {
                method: "PUT",

                headers: {
                    "Authorization":
                        "Bearer " + token,

                    "Content-Type":
                        "application/json"
                },

                body: JSON.stringify({
                    status: newStatus
                })
            }
        );


        if (response.status === 401 ||
            response.status === 403) {

            alert(
                "You are not authorized."
            );

            return;
        }


        if (!response.ok) {

            let message =
                "Unable to update order status.";


            try {

                const errorData =
                    await response.json();

                message =
                    errorData.message || message;

            } catch (e) {

                // Ignore JSON parsing error

            }


            alert(message);

            return;
        }


        alert(
            "Order status updated successfully!"
        );


        loadOrders();


    } catch (error) {

        console.error(error);

        alert(
            "Something went wrong."
        );

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