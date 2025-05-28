document.getElementById("registerForm").addEventListener("submit", async function (e) {
    e.preventDefault();

    const userData = {
        name: document.getElementById("username").value, // DTO'daki "name" ile eşleşmeli
        email: document.getElementById("email").value,
        password: document.getElementById("password").value
    };

    try {
        const response = await fetch("/api/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(userData)
        });

        const result = await response.text();
        document.getElementById("responseMessage").textContent = result;

        if (response.ok && result.toLowerCase().includes("başarılı")) {
            // E-posta bilgisini localStorage'a kaydet (verify.html'de kullanılacak)
            localStorage.setItem("registeredEmail", userData.email);

            // 1.5 saniye sonra yönlendir
            setTimeout(() => {
                window.location.href = "verify.html";
            }, 1500);
        }

    } catch (error) {
        console.error("Hata oluştu:", error);
        document.getElementById("responseMessage").textContent = "Kayıt başarısız oldu!";
    }
});
