package com.example.insurance.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${insurance.admin.email}")
    private String adminEmail;

    public void sendExpiryMail(
            String customerName,
            String customerEmail,
            String phone,
            String location,
            String vehicleInfo,
            String endDate,
            boolean expired
    ) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(adminEmail);
            helper.setSubject(
                    expired
                            ? "🚨 Insurance Policy EXPIRED"
                            : "⚠ Insurance Policy Expiring Soon"
            );

            helper.setText(
                    buildHtml(
                            customerName,
                            customerEmail,
                            phone,
                            location,
                            vehicleInfo,
                            endDate,
                            expired
                    ),
                    true
            );

            mailSender.send(message);

        } catch (MessagingException | MailException e) {
            throw new RuntimeException("Failed to send expiry mail", e);
        }
    }

    private String buildHtml(
            String name,
            String email,
            String phone,
            String location,
            String vehicle,
            String endDate,
            boolean expired
    ) {
        String statusColor = expired ? "#dc3545" : "#ff6b35";
        String statusText = expired ? "EXPIRED" : "EXPIRING SOON";
        String iconColor = expired ? "#ff6b6b" : "#4dabf7";
        String headerStart = expired ? "#ff6b6b" : "#ff922b";
        String headerEnd = expired ? "#c92a2a" : "#ff6b35";
        String priority = expired ? "HIGH" : "MEDIUM";
        String statusMessage = expired ? "has expired" : "is expiring soon";
        int currentYear = java.time.Year.now().getValue();

        // Escape % symbols in HTML content
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Insurance Alert</title>
                <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
            </head>
            <body style="margin:0; padding:0; font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%);">
                
                <!-- Main Container -->
                <div style="max-width:600px; margin:30px auto; padding:20px;">
                    
                    <!-- Card Container -->
                    <div style="background:#ffffff; border-radius:20px; box-shadow:0 20px 60px rgba(0,0,0,0.15); overflow:hidden;">
                        
                        <!-- Header with Gradient -->
                        <div style="background: linear-gradient(135deg, %s 0%%, %s 100%%); padding:40px 30px; text-align:center;">
                            <div style="display:inline-block; background:rgba(255,255,255,0.2); padding:15px; border-radius:50%%; margin-bottom:20px;">
                                <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2">
                                    <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"></path>
                                </svg>
                            </div>
                            <h1 style="margin:0; color:white; font-size:28px; font-weight:700; letter-spacing:-0.5px;">Insurance Alert</h1>
                            <p style="margin:10px 0 0 0; color:rgba(255,255,255,0.9); font-size:16px; font-weight:400;">Gateway Insurance System</p>
                        </div><br/>
                        
                        <!-- Status Badge -->
                        <div style="margin:-20px 30px 0; position:relative;">
                            <div style="display:inline-block; background:%s; color:white; padding:12px 28px; border-radius:50px; font-weight:600; font-size:14px; box-shadow:0 4px 15px rgba(0,0,0,0.15);">
                                ⚡ %s
                            </div>
                        </div>
                        
                        <!-- Content Area -->
                        <div style="padding:40px 30px;">
                            
                            <!-- Customer Info Section -->
                            <div style="margin-bottom:35px;">
                                <div style="display:flex; align-items:center; margin-bottom:20px;">
                                    <div style="background:%s10; padding:10px; border-radius:12px; margin-right:15px;">
                                        <svg width="24" height="24" viewBox="0 0 24 24" fill="%s" stroke="%s" stroke-width="2">
                                            <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
                                            <circle cx="12" cy="7" r="4"></circle>
                                        </svg>
                                    </div>
                                    <h2 style="margin:0; color:#2d3436; font-size:22px; font-weight:600;">Customer Information</h2>
                                </div>
                                
                                <!-- Info Grid -->
                                <div style="display:grid; grid-template-columns:repeat(auto-fit, minmax(250px, 1fr)); gap:20px;">
                                    
                                    <!-- Customer Details -->
                                    <div style="background:#f8f9fa; padding:25px; border-radius:15px; border-left:4px solid %s;">
                                        <table style="width:100%%; border-collapse:separate; border-spacing:0 12px;">
                                            <tr>
                                                <td style="padding:8px 0; font-weight:500; color:#6c757d; min-width:100px;">Name</td>
                                                <td style="padding:8px 0; font-weight:600; color:#2d3436;">%s</td>
                                            </tr>
                                            <tr>
                                                <td style="padding:8px 0; font-weight:500; color:#6c757d;">Email</td>
                                                <td style="padding:8px 0;">
                                                    <a href="mailto:%s" style="color:%s; text-decoration:none; font-weight:500;">%s</a>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="padding:8px 0; font-weight:500; color:#6c757d;">Mobile</td>
                                                <td style="padding:8px 0;">
                                                    <a href="tel:%s" style="color:#2d3436; text-decoration:none; font-weight:500;">%s</a>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="padding:8px 0; font-weight:500; color:#6c757d;">Location</td>
                                                <td style="padding:8px 0; font-weight:500; color:#2d3436;">%s</td>
                                            </tr>
                                        </table>
                                    </div><br/><br/>
                                    
                                    <!-- Insurance Details -->
                                    <div style="background:#f8f9fa; padding:25px; border-radius:15px; border-left:4px solid #20c997;">
                                        <table style="width:100%%; border-collapse:separate; border-spacing:0 12px;">
                                            <tr>
                                                <td style="padding:8px 0; font-weight:500; color:#6c757d; min-width:100px;">Vehicle</td>
                                                <td style="padding:8px 0; font-weight:600; color:#2d3436;">%s</td>
                                            </tr>
                                            <tr>
                                                <td style="padding:8px 0; font-weight:500; color:#6c757d;">Expiry Date</td>
                                                <td style="padding:8px 0;">
                                                    <div style="display:inline-flex; align-items:center; background:%s15; color:%s; padding:6px 14px; border-radius:8px; font-weight:600;">
                                                        <svg style="margin-right:6px;" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                                            <rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect>
                                                            <line x1="16" y1="2" x2="16" y2="6"></line>
                                                            <line x1="8" y1="2" x2="8" y2="6"></line>
                                                            <line x1="3" y1="10" x2="21" y2="10"></line>
                                                        </svg>
                                                        %s
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="padding:8px 0; font-weight:500; color:#6c757d;">Status</td>
                                                <td style="padding:8px 0;">
                                                    <span style="display:inline-block; background:%s20; color:%s; padding:6px 16px; border-radius:20px; font-weight:600; font-size:13px;">
                                                        %s
                                                    </span>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="padding:8px 0; font-weight:500; color:#6c757d;">Priority</td>
                                                <td style="padding:8px 0;">
                                                    <span style="display:inline-block; background:%s; color:white; padding:6px 16px; border-radius:20px; font-weight:600; font-size:13px;">
                                                        %s
                                                    </span>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                    
                                </div>
                            </div>
                            
                            <!-- Action Section -->
                            <div style="background:linear-gradient(135deg, #f8f9fa 0%%, #e9ecef 100%%); padding:30px; border-radius:15px; text-align:center;">
                                <h3 style="margin:0 0 20px 0; color:#2d3436; font-size:20px; font-weight:600;">🚨 Required Action</h3>
                                <p style="margin:0 0 25px 0; color:#6c757d; line-height:1.6; font-size:16px;">
                                    This insurance policy %s. Please contact the customer immediately to discuss renewal options.
                                </p>
                                <div style="display:flex; gap:15px; justify-content:center; flex-wrap:wrap;">
                                    <a href="mailto:%s?subject=Insurance%%20Renewal%%20-%s" style="background:%s; color:white; padding:14px 32px; border-radius:10px; text-decoration:none; font-weight:600; font-size:15px; transition:all 0.3s ease;">
                                        📧 Send Email
                                    </a>
                                    <a href="tel:%s" style="background:#2d3436; color:white; padding:14px 32px; border-radius:10px; text-decoration:none; font-weight:600; font-size:15px; transition:all 0.3s ease;">
                                        📞 Call Customer
                                    </a>
                                </div>
                            </div>
                            
                            <!-- Footer -->
                            <div style="margin-top:40px; padding-top:25px; border-top:1px solid #e9ecef; text-align:center;">
                                <div style="color:#6c757d; font-size:14px; line-height:1.6;">
                                    <p style="margin:0 0 10px 0;">
                                        <strong>Gateway Insurance Company</strong><br>
                                        123 Business Ave, Suite 100 • City, State 12345
                                    </p>
                                    <p style="margin:0; font-size:12px; color:#adb5bd;">
                                        This is an automated notification. Please do not reply to this email.<br>
                                        © %s Gateway Insurance. All rights reserved.
                                    </p>
                                </div>
                            </div>
                            
                        </div>
                        
                    </div>
                    
                </div>
                
            </body>
            </html>
            """,
            // Format specifiers in order:
            headerStart, headerEnd,          // 1, 2: Header gradient colors
            statusColor,                     // 3: Status badge background
            statusText,                      // 4: Status text
            iconColor,                       // 5: Icon background (with 10 opacity)
            iconColor,                       // 6: Icon fill color
            iconColor,                       // 7: Icon stroke color
            iconColor,                       // 8: Border color
            name,                            // 9: Customer name
            email,                           // 10: Customer email (for mailto)
            iconColor,                       // 11: Email link color
            email,                           // 12: Email display text
            phone,                           // 13: Phone number (for tel)
            phone,                           // 14: Phone display text
            location,                        // 15: Location
            vehicle,                         // 16: Vehicle info
            statusColor,                     // 17: Expiry date background
            statusColor,                     // 18: Expiry date text color
            endDate,                         // 19: Expiry date
            statusColor,                     // 20: Status background
            statusColor,                     // 21: Status text color
            statusText,                      // 22: Status display text
            statusColor,                     // 23: Priority background
            priority,                        // 24: Priority text
            statusMessage,                   // 25: Status message
            email,                           // 26: Email for mailto link
            name,                            // 27: Customer name for subject
            statusColor,                     // 28: Email button color
            phone,                           // 29: Phone for tel link
            currentYear                      // 30: Current year
        );
    }
}