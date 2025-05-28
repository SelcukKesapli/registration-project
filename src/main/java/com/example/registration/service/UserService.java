package com.example.registration.service;

import com.example.registration.dto.UserDto;
import com.example.registration.entity.User;
import com.example.registration.entity.VerificationCode;
import com.example.registration.repository.UserRepository;
import com.example.registration.repository.VerificationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service  // Spring bu sınıfı servis olarak tanır
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;


    @Autowired
    public UserService(UserRepository userRepository,VerificationCodeRepository verificationCodeRepository
    , EmailService emailService) {
        this.userRepository = userRepository;
        this.verificationCodeRepository = verificationCodeRepository;
        this.emailService = emailService;
        this.passwordEncoder = new BCryptPasswordEncoder();  // Şifreleyici
    }

    private String generateVerificationCode() {
        int random = (int)(Math.random() * 900000) + 100000; // 100000 - 999999
        return String.valueOf(random);
    }


    public String registerUser(UserDto userDto) {
        // Aynı e-posta daha önce kayıt olmuş mu?
        if (userRepository.existsByEmail(userDto.getEmail())) {
            return "Bu e-posta adresi zaten kullanılıyor.";
        }

        // Yeni kullanıcı nesnesi oluştur
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());

        // Şifreyi BCrypt ile şifrele
        String encryptedPassword = passwordEncoder.encode(userDto.getPassword());
        user.setPassword(encryptedPassword);

        // enabled = false, çünkü daha doğrulama yapılmadı
        user.setEnabled(false);

        // Kullanıcıyı kaydet
        userRepository.save(user);

        // Burada ileride doğrulama kodu üretip mail göndereceğiz

        // Kod üret
        String code = generateVerificationCode();

// Kod objesini oluştur
        VerificationCode verification = new VerificationCode();
        verification.setEmail(user.getEmail());
        verification.setCode(code);
        verification.setExpiryTime(LocalDateTime.now().plusMinutes(15)); // 15 dk geçerli

// Veritabanına kaydet
        verificationCodeRepository.save(verification);

// E-posta gönder
        emailService.sendVerificationCode(user.getEmail(), code);


        return "Kayıt başarılı. Doğrulama kodu e-posta adresinize gönderildi.";
    }

    public String verifyUser(String email, String code) {
        Optional<VerificationCode> optionalCode = verificationCodeRepository.findByEmail(email);

        if (optionalCode.isEmpty()) {
            return "Doğrulama kodu bulunamadı. Lütfen önce kayıt olun.";
        }

        VerificationCode verification = optionalCode.get();

        if (!verification.getCode().equals(code)) {
            return "Geçersiz doğrulama kodu.";
        }

        if (verification.getExpiryTime().isBefore(java.time.LocalDateTime.now())) {
            return "Doğrulama kodunun süresi dolmuş.";
        }

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEnabled(true);
            userRepository.save(user);
            verificationCodeRepository.delete(verification); // kodu artık sil
            return "Doğrulama başarılı. Hesabınız aktifleştirildi!";
        }

        return "Kullanıcı bulunamadı.";
    }
}
