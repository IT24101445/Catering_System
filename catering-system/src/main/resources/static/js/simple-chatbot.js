// Simple Chatbot Test
console.log('Simple chatbot script loaded!');

document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM loaded, creating simple chatbot...');
    
    // Create simple chatbot HTML
    const chatbotHTML = `
        <div id="chatbot-container" style="position: fixed; bottom: 20px; right: 20px; z-index: 1000;">
            <div id="chatbot-toggle" style="display: flex; align-items: center; background: #FFD700; color: white; padding: 15px 20px; border-radius: 50px; cursor: pointer; box-shadow: 0 4px 20px rgba(255, 215, 0, 0.3);">
                <i class="fas fa-robot" style="font-size: 20px; margin-right: 10px;"></i>
                <span>Chat with us!</span>
            </div>
        </div>
    `;
    
    document.body.insertAdjacentHTML('beforeend', chatbotHTML);
    
    // Add click event
    document.getElementById('chatbot-toggle').addEventListener('click', function() {
        alert('Hello! This is a simple chatbot test. The full chatbot will be implemented here.');
    });
    
    console.log('Simple chatbot created successfully!');
});

