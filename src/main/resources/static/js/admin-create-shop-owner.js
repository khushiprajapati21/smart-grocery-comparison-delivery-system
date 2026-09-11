document.addEventListener("DOMContentLoaded", function () {

    const shopOwnerForm =
        document.getElementById("shopOwnerForm");

    if (!shopOwnerForm) {
        return;
    }

    shopOwnerForm.addEventListener("submit", async function (event) {

        event.preventDefault();

        const name =
            document.getElementById("name").value.trim();

        const email =
            document.getElementById("email").value.trim();

        const phone =
            document.getElementById("phone").value.trim();

        const password =
            document.getElementById("password").value;

        const confirmPassword =
            document.getElementById("confirmPassword").value;


        // Password validation
        if (password !== confirmPassword) {

            alert("Passwords do not match.");

            return;
        }


        // Get admin JWT
        const token = localStorage.getItem("token");

        if (!token) {

            alert("Admin session not found. Please login again.");

            window.location.href = "/login";

            return;
        }


        const shopOwnerData = {

            name: name,
            email: email,
            phone: phone,
            password: password

        };


        try {

            const response = await fetch("/admin/shop-owner", {

                method: "POST",

                headers: {

                    "Content-Type": "application/json",

                    "Authorization": "Bearer " + token

                },

                body: JSON.stringify(shopOwnerData)

            });


            if (response.ok) {

                const createdUser =
                    await response.json();

                alert(
                    "Shop Owner created successfully!\n\n" +
                    "Name: " + createdUser.name + "\n" +
                    "Email: " + createdUser.email
                );

                shopOwnerForm.reset();

            } else {

                const errorText =
                    await response.text();

                alert(
                    errorText ||
                    "Failed to create Shop Owner."
                );
            }

        } catch (error) {

            console.error(
                "Create Shop Owner error:",
                error
            );

            alert(
                "Something went wrong. Please try again."
            );
        }

    });

});