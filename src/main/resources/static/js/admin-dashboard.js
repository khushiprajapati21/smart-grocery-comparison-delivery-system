document.addEventListener("DOMContentLoaded", async function () {

const adminName =
    localStorage.getItem("name");

if (adminName) {
    document.getElementById("adminName").textContent =
        adminName;
}

const token =
    localStorage.getItem("token");

if (!token) {

    alert("Admin session not found. Please login again.");

    window.location.href = "/login";

    return;
}

try {

    const response = await fetch("/admin/dashboard", {

        method: "GET",

        headers: {
            "Authorization": "Bearer " + token
        }

    });

    if (response.status === 403) {

        alert("Access denied. Admin access required.");

        window.location.href = "/login";

        return;
    }

    if (!response.ok) {

        throw new Error("Failed to load dashboard");

    }

    const data = await response.json();

    document.getElementById("totalUsers").textContent =
        data.totalUsers;

    document.getElementById("totalCustomers").textContent =
        data.totalCustomers;

    document.getElementById("totalShopOwners").textContent =
        data.totalShopOwners;

    document.getElementById("totalOrders").textContent =
        data.totalOrders;

} catch (error) {

    console.error(
        "Admin dashboard error:",
        error
    );

    alert(
        "Unable to load admin dashboard."
    );
}


});

function logout() {

localStorage.clear();

window.location.href = "/login";

}
