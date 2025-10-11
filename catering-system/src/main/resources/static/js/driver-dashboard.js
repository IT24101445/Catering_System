document.addEventListener('DOMContentLoaded', function() {
    // Check if driver is logged in
    const driverData = sessionStorage.getItem('driver');
    if (!driverData) {
        window.location.href = '/driver/login';
        return;
    }

    const driver = JSON.parse(driverData);
    document.getElementById('driverName').textContent = driver.name;
    document.getElementById('driverWelcomeName').textContent = driver.name;

    // Load driver assignments and notifications
    loadAssignments(driver.id);
    loadNotifications(driver.id);
});

async function loadAssignments(driverId) {
    try {
        const response = await fetch(`/api/assignments?driverId=${driverId}`);
        if (response.ok) {
            const assignments = await response.json();
            displayAssignments(assignments);
            updateStatusCounts(assignments);
        } else {
            console.error('Failed to load assignments');
            document.getElementById('assignmentsList').innerHTML = 
                '<div class="text-center text-red-500">Failed to load assignments</div>';
        }
    } catch (error) {
        console.error('Error loading assignments:', error);
        document.getElementById('assignmentsList').innerHTML = 
            '<div class="text-center text-red-500">Error loading assignments</div>';
    }
}

function displayAssignments(assignments) {
    const container = document.getElementById('assignmentsList');
    
    if (assignments.length === 0) {
        container.innerHTML = '<div class="text-center text-gray-500">No assignments found</div>';
        return;
    }

    container.innerHTML = assignments.map(assignment => `
        <div class="assignment-card card ${getAssignmentStatusClass(assignment)}">
            <div class="flex justify-between items-start">
                <div class="flex-1">
                    <h3 class="font-semibold text-lg">Order #${assignment.orderId}</h3>
                    <p class="text-gray-600">Customer: ${assignment.orderCustomerName}</p>
                    <p class="text-gray-600">Route: ${assignment.route}</p>
                    <p class="text-sm text-gray-500">Assignment ID: ${assignment.id}</p>
                </div>
                <div class="flex flex-col gap-2">
                    ${getAssignmentButtons(assignment)}
                </div>
            </div>
        </div>
    `).join('');
}

function getAssignmentStatusClass(assignment) {
    switch (assignment.status) {
        case 'ACCEPTED':
            return 'accepted';
        case 'COMPLETED':
            return 'completed';
        case 'DECLINED':
            return 'declined';
        default:
            return '';
    }
}

function getAssignmentButtons(assignment) {
    switch (assignment.status) {
        case 'PENDING':
            return `
                <button onclick="acceptAssignment(${assignment.id})" class="btn btn-success">
                    Accept
                </button>
                <button onclick="declineAssignment(${assignment.id})" class="btn btn-danger">
                    Decline
                </button>
            `;
        case 'ACCEPTED':
            return `
                <button onclick="startDelivery(${assignment.id})" class="btn">
                    Start Delivery
                </button>
            `;
        case 'IN_PROGRESS':
            return `
                <button onclick="completeDelivery(${assignment.id})" class="btn btn-success">
                    Complete Delivery
                </button>
            `;
        case 'COMPLETED':
            return `<span class="text-green-600 font-semibold">Completed</span>`;
        case 'DECLINED':
            return `<span class="text-red-600 font-semibold">Declined</span>`;
        default:
            return '';
    }
}

function updateStatusCounts(assignments) {
    const pending = assignments.filter(a => a.status === 'PENDING').length;
    const accepted = assignments.filter(a => a.status === 'ACCEPTED').length;
    const completed = assignments.filter(a => a.status === 'COMPLETED').length;

    document.getElementById('pendingCount').textContent = pending;
    document.getElementById('acceptedCount').textContent = accepted;
    document.getElementById('completedCount').textContent = completed;
}

async function acceptAssignment(assignmentId) {
    try {
        const response = await fetch(`/api/assignments/${assignmentId}/accept`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            }
        });

        if (response.ok) {
            // Reload assignments to show updated status
            const driverData = JSON.parse(sessionStorage.getItem('driver'));
            loadAssignments(driverData.id);
        } else {
            const error = await response.json();
            alert('Failed to accept assignment: ' + (error.message || 'Unknown error'));
        }
    } catch (error) {
        console.error('Error accepting assignment:', error);
        alert('Network error. Please try again.');
    }
}

async function declineAssignment(assignmentId) {
    try {
        const response = await fetch(`/api/assignments/${assignmentId}/decline`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            }
        });

        if (response.ok) {
            // Reload assignments to show updated status
            const driverData = JSON.parse(sessionStorage.getItem('driver'));
            loadAssignments(driverData.id);
        } else {
            const error = await response.json();
            alert('Failed to decline assignment: ' + (error.message || 'Unknown error'));
        }
    } catch (error) {
        console.error('Error declining assignment:', error);
        alert('Network error. Please try again.');
    }
}

async function startDelivery(assignmentId) {
    try {
        const response = await fetch(`/api/assignments/${assignmentId}/start`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            }
        });

        if (response.ok) {
            // Reload assignments to show updated status
            const driverData = JSON.parse(sessionStorage.getItem('driver'));
            loadAssignments(driverData.id);
        } else {
            const error = await response.json();
            alert('Failed to start delivery: ' + (error.message || 'Unknown error'));
        }
    } catch (error) {
        console.error('Error starting delivery:', error);
        alert('Network error. Please try again.');
    }
}

async function completeDelivery(assignmentId) {
    try {
        const response = await fetch(`/api/assignments/${assignmentId}/complete`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            }
        });

        if (response.ok) {
            // Reload assignments to show updated status
            const driverData = JSON.parse(sessionStorage.getItem('driver'));
            loadAssignments(driverData.id);
        } else {
            const error = await response.json();
            alert('Failed to complete delivery: ' + (error.message || 'Unknown error'));
        }
    } catch (error) {
        console.error('Error completing delivery:', error);
        alert('Network error. Please try again.');
    }
}

async function loadNotifications(driverId) {
    try {
        const response = await fetch(`/api/notifications/driver/${driverId}`);
        if (response.ok) {
            const notifications = await response.json();
            displayNotifications(notifications);
        } else {
            console.error('Failed to load notifications');
        }
    } catch (error) {
        console.error('Error loading notifications:', error);
    }
}

function displayNotifications(notifications) {
    const container = document.getElementById('notificationsList');
    
    if (notifications.length === 0) {
        container.innerHTML = '<div class="text-center text-gray-500">No notifications</div>';
        return;
    }

    container.innerHTML = notifications.map(notification => `
        <div class="notification-item p-3 border rounded-lg ${notification.isRead ? 'bg-gray-50' : 'bg-blue-50 border-blue-200'}">
            <div class="flex justify-between items-start">
                <div class="flex-1">
                    <p class="text-sm font-medium ${notification.isRead ? 'text-gray-600' : 'text-blue-800'}">${notification.message}</p>
                    <p class="text-xs text-gray-500 mt-1">${new Date(notification.createdAt).toLocaleString()}</p>
                </div>
                ${!notification.isRead ? `<button onclick="markAsRead(${notification.id})" class="text-xs text-blue-600 hover:text-blue-800">Mark Read</button>` : ''}
            </div>
        </div>
    `).join('');
}

async function markAsRead(notificationId) {
    try {
        const response = await fetch(`/api/notifications/${notificationId}/mark-read`, {
            method: 'POST'
        });
        if (response.ok) {
            // Reload notifications
            const driverData = JSON.parse(sessionStorage.getItem('driver'));
            loadNotifications(driverData.id);
        }
    } catch (error) {
        console.error('Error marking notification as read:', error);
    }
}

async function markAllAsRead() {
    try {
        const driverData = JSON.parse(sessionStorage.getItem('driver'));
        const response = await fetch(`/api/notifications/driver/${driverData.id}/mark-all-read`, {
            method: 'POST'
        });
        if (response.ok) {
            // Reload notifications
            loadNotifications(driverData.id);
        }
    } catch (error) {
        console.error('Error marking all notifications as read:', error);
    }
}

// Add event listener for mark all read button
document.addEventListener('DOMContentLoaded', function() {
    const markAllReadBtn = document.getElementById('markAllReadBtn');
    if (markAllReadBtn) {
        markAllReadBtn.addEventListener('click', markAllAsRead);
    }
});
