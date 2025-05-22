<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <link rel="shortcut icon" type="x-icon" href="${pageContext.request.contextPath}/resources/logo/logo.png">
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/contact.css" />
    <title>Contact Us</title>
  </head>
  <body>
  
  <jsp:include page="header.jsp" />
  
  <div class="main">
     <div class="contact-sidebar">
       <h3>CONTACT ONEZ</h3>
     </div>
    <div class="contact-container">
      <div class="contact-section">
        <h2>How to Reach Us</h2>
        <p>
          For inquiries, support, or feedback, feel free to get in touch via the
          following channels:
        </p>
        <ul>
          <li>Email: oneZsupport@gmail.com</li>
          <li>Phone: +977-9813747090</li>
        </ul>
      </div>

      <div class="contact-section">
        <h2>Support Hours</h2>
        <p>Sunday to Friday: 8 am - 5 pm</p>
      </div>
	<!-- Notification element -->
	  <div id="notification" class="notification">
	    <span id="notification-message">Your message has been sent successfully!</span>
	    <span class="close-btn" onclick="hideNotification()">&times;</span>
	  </div>
      <div class="contact-section">
        <h2>Feedback</h2>
        <p>
          Please contact us if you have any inconveniences and share your
          thoughts and suggestions to help us improve.
        </p>
      </div>
      <div class="contact-section1">
        <h2 class="help">Fill up the form for inquiry</h2>
        <form action="#" method="post" class="contact-form" id="contactForm">
          <label for="subject">Subject</label>
          <input
            type="text"
            id="subject"
            name="subject"
            placeholder="Subject"
            required
          />
          <label for="message">Your Message</label>
          <textarea
            id="message"
            name="message"
            placeholder="Your Message"
            required
          ></textarea>
          <button type="submit">Submit</button>
        </form>
      </div>
    </div>
    </div>
    <jsp:include page="footer.jsp" />
    <script>
      document.getElementById('contactForm').addEventListener('submit', function(e) {
        e.preventDefault(); // Prevent actual form submission
        
        // Show notification
        showNotification();
        
        // Reset form
        this.reset();
      });
      
      function showNotification() {
        const notification = document.getElementById('notification');
        notification.style.display = 'block';
        
        // Hide notification after 5 seconds
        setTimeout(hideNotification, 5000);
      }
      
      function hideNotification() {
        document.getElementById('notification').style.display = 'none';
      }
    </script>
  </body>
</html>
