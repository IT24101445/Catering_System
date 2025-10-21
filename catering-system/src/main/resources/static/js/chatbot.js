// Golden Dish Catering Chatbot with Time-based Greetings
class CateringChatbot {
    constructor() {
        this.isOpen = false;
        this.messages = [];
        this.currentTime = new Date();
        this.greeting = this.getTimeBasedGreeting();
        
        this.init();
    }
    
    init() {
        this.createChatbotHTML();
        this.bindEvents();
        this.addWelcomeMessage();
    }
    
    getTimeBasedGreeting() {
        const hour = this.currentTime.getHours();
        
        if (hour >= 5 && hour < 12) {
            return "Good Morning! ☀️";
        } else if (hour >= 12 && hour < 17) {
            return "Good Afternoon! 🌞";
        } else if (hour >= 17 && hour < 21) {
            return "Good Evening! 🌆";
        } else {
            return "Good Night! 🌙";
        }
    }
    
    createChatbotHTML() {
        const chatbotHTML = `
            <div id="chatbot-container">
                <div id="chatbot-toggle" class="chatbot-toggle">
                    <i class="fas fa-robot"></i>
                    <span class="chatbot-status">Online</span>
                </div>
                
                <div id="chatbot-window" class="chatbot-window">
                    <div class="chatbot-header">
                        <div class="chatbot-avatar">
                            <i class="fas fa-robot"></i>
                        </div>
                        <div class="chatbot-info">
                            <h4>Golden Dish Assistant</h4>
                            <span class="chatbot-status-dot"></span>
                            <span>Online</span>
                        </div>
                        <button id="chatbot-close" class="chatbot-close">
                            <i class="fas fa-times"></i>
                        </button>
                    </div>
                    
                    <div class="chatbot-messages" id="chatbot-messages">
                        <!-- Messages will be added here -->
                    </div>
                    
                    <div class="chatbot-input-container">
                        <input type="text" id="chatbot-input" placeholder="Type your message..." autocomplete="off">
                        <button id="chatbot-send" class="chatbot-send">
                            <i class="fas fa-paper-plane"></i>
                        </button>
                    </div>
                </div>
            </div>
        `;
        
        document.body.insertAdjacentHTML('beforeend', chatbotHTML);
    }
    
    bindEvents() {
        const toggle = document.getElementById('chatbot-toggle');
        const close = document.getElementById('chatbot-close');
        const send = document.getElementById('chatbot-send');
        const input = document.getElementById('chatbot-input');
        
        toggle.addEventListener('click', () => this.toggleChatbot());
        close.addEventListener('click', () => this.closeChatbot());
        send.addEventListener('click', () => this.sendMessage());
        input.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') {
                this.sendMessage();
            }
        });
    }
    
    toggleChatbot() {
        this.isOpen = !this.isOpen;
        const window = document.getElementById('chatbot-window');
        const toggle = document.getElementById('chatbot-toggle');
        
        if (this.isOpen) {
            window.style.display = 'block';
            toggle.style.display = 'none';
            document.getElementById('chatbot-input').focus();
        } else {
            window.style.display = 'none';
            toggle.style.display = 'flex';
        }
    }
    
    closeChatbot() {
        this.isOpen = false;
        document.getElementById('chatbot-window').style.display = 'none';
        document.getElementById('chatbot-toggle').style.display = 'flex';
    }
    
    addWelcomeMessage() {
        const welcomeMessages = [
            `${this.greeting} Welcome to Golden Dish Catering! 🍽️`,
            "I'm here to help you with any questions about our catering services.",
            "You can ask me about our menu, booking process, or any other inquiries!"
        ];
        
        welcomeMessages.forEach((message, index) => {
            setTimeout(() => {
                this.addBotMessage(message);
            }, index * 1000);
        });
    }
    
    addBotMessage(message) {
        const messagesContainer = document.getElementById('chatbot-messages');
        const messageElement = document.createElement('div');
        messageElement.className = 'chatbot-message chatbot-message-bot';
        messageElement.innerHTML = `
            <div class="chatbot-avatar-small">
                <i class="fas fa-robot"></i>
            </div>
            <div class="chatbot-message-content">
                <div class="chatbot-message-text">${message}</div>
                <div class="chatbot-message-time">${this.getCurrentTime()}</div>
            </div>
        `;
        
        messagesContainer.appendChild(messageElement);
        messagesContainer.scrollTop = messagesContainer.scrollHeight;
    }
    
    addUserMessage(message) {
        const messagesContainer = document.getElementById('chatbot-messages');
        const messageElement = document.createElement('div');
        messageElement.className = 'chatbot-message chatbot-message-user';
        messageElement.innerHTML = `
            <div class="chatbot-message-content">
                <div class="chatbot-message-text">${message}</div>
                <div class="chatbot-message-time">${this.getCurrentTime()}</div>
            </div>
            <div class="chatbot-avatar-small">
                <i class="fas fa-user"></i>
            </div>
        `;
        
        messagesContainer.appendChild(messageElement);
        messagesContainer.scrollTop = messagesContainer.scrollHeight;
    }
    
    sendMessage() {
        const input = document.getElementById('chatbot-input');
        const message = input.value.trim();
        
        if (message) {
            this.addUserMessage(message);
            input.value = '';
            
            // Simulate bot response
            setTimeout(() => {
                this.handleBotResponse(message);
            }, 1000);
        }
    }
    
    handleBotResponse(userMessage) {
        const responses = this.getBotResponses(userMessage.toLowerCase());
        const response = responses[Math.floor(Math.random() * responses.length)];
        this.addBotMessage(response);
    }
    
    getBotResponses(message) {
        const responses = {
            greeting: [
                `${this.greeting} How can I help you today?`,
                "Hello! I'm here to assist you with our catering services.",
                "Hi there! What would you like to know about Golden Dish Catering?"
            ],
            menu: [
                "We offer a wide variety of dishes including Indian, Continental, Chinese, and Fusion cuisine! 🍛",
                "Our menu includes appetizers, main courses, desserts, and beverages. Would you like to see our seasonal specials?",
                "We have vegetarian and non-vegetarian options available. Our chefs can also customize dishes according to your preferences!"
            ],
            booking: [
                "To book our catering services, you can contact us at +91-9876543210 or email us at info@goldendish.com 📞",
                "We require at least 24 hours notice for bookings. For large events, we recommend booking a week in advance.",
                "You can also visit our website to fill out the booking form with your event details and preferences."
            ],
            pricing: [
                "Our pricing depends on the menu selection, number of guests, and event type. We offer competitive rates! 💰",
                "We provide detailed quotes based on your requirements. Contact us for a personalized estimate.",
                "We have different packages available - from budget-friendly to premium options."
            ],
            hours: [
                "We're open Monday to Sunday, 9:00 AM to 10:00 PM 🕘",
                "Our kitchen operates from 6:00 AM to 11:00 PM for fresh food preparation.",
                "For emergency catering needs, we have 24/7 customer support available."
            ],
            default: [
                "I'm here to help! You can ask me about our menu, booking process, pricing, or any other catering-related questions.",
                "That's a great question! Let me connect you with our team for more detailed information.",
                "I'd be happy to help you with that. Could you provide more details about your catering needs?"
            ]
        };
        
        if (message.includes('hello') || message.includes('hi') || message.includes('hey')) {
            return responses.greeting;
        } else if (message.includes('menu') || message.includes('food') || message.includes('dish')) {
            return responses.menu;
        } else if (message.includes('book') || message.includes('order') || message.includes('reserve')) {
            return responses.booking;
        } else if (message.includes('price') || message.includes('cost') || message.includes('rate')) {
            return responses.pricing;
        } else if (message.includes('time') || message.includes('hour') || message.includes('open')) {
            return responses.hours;
        } else {
            return responses.default;
        }
    }
    
    getCurrentTime() {
        return new Date().toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'});
    }
}

// Initialize chatbot when page loads
document.addEventListener('DOMContentLoaded', function() {
    new CateringChatbot();
});

