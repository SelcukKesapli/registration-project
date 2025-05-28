document.getElementById("verifyForm").addEventListener("submit", async function (e) {
    e.preventDefault();

    const email = localStorage.getItem("registeredEmail"); // localStorage'dan al
    const code = document.getElementById("code").value;

    if (!email) {
        document.getElementById("verifyMessage").textContent = "E-posta bilgisi eksik. Lütfen kayıt işlemini tekrar yapın.";
        return;
    }

    const data = {
        email: email,
        code: code
    };

    try {
        const response = await fetch("/api/verify", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        });

        const result = await response.text();
        const messageBox = document.getElementById("verifyMessage");

        messageBox.textContent = result;

        if (response.ok && result.toLowerCase().includes("başarılı")) {
            messageBox.classList.remove("text-danger");
            messageBox.classList.add("text-success");

            // localStorage temizlenebilir istenirse
            localStorage.removeItem("registeredEmail");
        } else {
            messageBox.classList.remove("text-success");
            messageBox.classList.add("text-danger");
        }

    } catch (error) {
        console.error("Hata:", error);
        const messageBox = document.getElementById("verifyMessage");
        messageBox.textContent = "Doğrulama sırasında bir hata oluştu!";
        messageBox.classList.remove("text-success");
        messageBox.classList.add("text-danger");
    }
});
