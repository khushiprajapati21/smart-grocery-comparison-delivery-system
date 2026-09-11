document.addEventListener("DOMContentLoaded", function () {

    const registerForm = document.getElementById("registerForm");

    if (!registerForm) {
        return;
    }

    registerForm.addEventListener("submit", async function (event) {

        event.preventDefault();

        const name = document.getElementById("name").value.trim();
        const email = document.getElementById("email").value.trim();
        const phone = document.getElementById("phone").value.trim();
        const password = document.getElementById("password").value;
        const confirmPassword = document.getElementById("confirmPassword").value;

        // Password confirmation check
        if (password !== confirmPassword) {
            alert("Passwords do not match.");
            return;
        }

        const registerData = {
            name: name,
            email: email,
            phone: phone,
            password: password,
            role: "CUSTOMER"
        };

        try {

            const response = await fetch("/auth/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(registerData)
            });

            if (response.ok) {

                alert("Registration successful! Please login.");

                window.location.href = "/login";

            } else {

                const errorText = await response.text();

                alert(
                    errorText || "Registration failed. Please try again."
                );
            }

        } catch (error) {

            console.error("Registration error:", error);

            alert("Something went wrong. Please try again.");
        }

    });

});