document.getElementById("loginForm")
.addEventListener("submit", async function (e) {

    e.preventDefault();

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    try {

        const response = await fetch("/auth/login", {

            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify({
                email: email,
                password: password
            })

        });

        if (!response.ok) {

            alert("Invalid Email or Password");
            return;

        }

        const data = await response.json();

        // Save Login Details

        localStorage.setItem("token", data.token);
        localStorage.setItem("customerId", data.id);
        localStorage.setItem("role", data.role);
        localStorage.setItem("name", data.name);

        alert("Login Successful");

        // Redirect

		if (data.role === "ADMIN") {

		    window.location.href = "/admin/dashboard-page";

        } else if (data.role === "SHOP_OWNER") {

            window.location.href = "/shop/dashboard";

        } else if (data.role === "DELIVERY_AGENT") {

            window.location.href = "/delivery/dashboard";

        } else {

            // CUSTOMER
            window.location.href = "/products";

        }

    } catch (error) {

        alert("Something went wrong.");

    }

});