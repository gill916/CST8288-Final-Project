// Toggle user type fields in registration form
function toggleUserTypeFields() {
    const userType = document.querySelector('select[name="userType"]').value;
    const professionalFields = document.getElementById('professionalFields');
    const institutionFields = document.getElementById('institutionFields');
    
    if (professionalFields && institutionFields) {
        professionalFields.style.display = userType === 'Professional' ? 'block' : 'none';
        institutionFields.style.display = userType === 'Institution' ? 'block' : 'none';
    }
}

// Validate registration form fields based on user type
function validateForm() {
    const userType = document.querySelector('select[name="userType"]').value;
    if (!userType) {
        alert('Please select a user type');
        return false;
    }

    if (userType === 'Professional') {
        const required = ['firstName', 'lastName', 'currentInstitution', 'academicPosition'];
        return validateRequiredFields(required);
    } else if (userType === 'Institution') {
        const required = ['institutionName', 'address', 'contactEmail'];
        return validateRequiredFields(required);
    }
    return false;
}

// Helper function to validate required fields
function validateRequiredFields(fields) {
    for (const field of fields) {
        const input = document.querySelector(`[name="${field}"]`);
        if (!input || !input.value.trim()) {
            alert(`Please fill in the ${field.replace(/([A-Z])/g, ' $1').toLowerCase()}`);
            if (input) input.focus();
            return false;
        }
    }
    return true;
}

// Handle application status updates
function updateStatus(applicationId, status) {
    if (confirm('Are you sure you want to ' + status.toLowerCase() + ' this application?')) {
        const form = document.createElement('form');
        form.method = 'POST';
        form.action = '/application/updateStatus';
        
        const applicationIdInput = document.createElement('input');
        applicationIdInput.type = 'hidden';
        applicationIdInput.name = 'applicationId';
        applicationIdInput.value = applicationId;
        
        const statusInput = document.createElement('input');
        statusInput.type = 'hidden';
        statusInput.name = 'status';
        statusInput.value = status;
        
        form.appendChild(applicationIdInput);
        form.appendChild(statusInput);
        document.body.appendChild(form);
        form.submit();
    }
}

// Auto-dismiss alerts after 5 seconds
document.addEventListener('DOMContentLoaded', function() {
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.opacity = '0';
            setTimeout(() => alert.remove(), 300);
        }, 5000);
    });
});

// Prevent form double submission
document.addEventListener('DOMContentLoaded', function() {
    const forms = document.querySelectorAll('form');
    forms.forEach(form => {
        form.addEventListener('submit', function() {
            const submitButton = form.querySelector('button[type="submit"]');
            if (submitButton) {
                submitButton.disabled = true;
                submitButton.innerHTML = 'Processing...';
            }
        });
    });
});

// Profile form validation
function validateProfileForm(userType) {
    const requiredFields = userType === 'PROFESSIONAL' 
        ? ['firstName', 'lastName', 'currentInstitution', 'position', 'educationBackground', 'expertise']
        : ['institutionName', 'address', 'contactEmail', 'contactPhone'];
        
    return validateRequiredFields(requiredFields);
}

// Form submission handling
document.addEventListener('DOMContentLoaded', function() {
    const forms = document.querySelectorAll('form');
    forms.forEach(form => {
        form.addEventListener('submit', function() {
            const submitButton = form.querySelector('button[type="submit"]');
            if (submitButton) {
                submitButton.disabled = true;
                submitButton.innerHTML = 'Processing...';
            }
        });
    });
});

// Notification Functions
function updateNotificationCount() {
    fetch('/notification/count')
        .then(response => response.json())
        .then(data => {
            const badge = document.querySelector('#notificationBadge');
            if (badge) {
                if (data.count > 0) {
                    badge.textContent = data.count;
                    badge.style.display = 'inline';
                } else {
                    badge.style.display = 'none';
                }
            }
        });
}

function markNotificationAsRead(notificationId) {
    fetch('/notification', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `action=markRead&notificationId=${notificationId}`
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            const notification = document.querySelector(`#notification-${notificationId}`);
            if (notification) {
                notification.classList.remove('unread');
                updateNotificationCount();
            }
        }
    });
}

function deleteNotification(notificationId) {
    if (confirm('Are you sure you want to delete this notification?')) {
        fetch('/notification', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `action=delete&notificationId=${notificationId}`
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                const notification = document.querySelector(`#notification-${notificationId}`);
                if (notification) {
                    notification.remove();
                    updateNotificationCount();
                }
            }
        });
    }
}

function markAllNotificationsAsRead() {
    fetch('/notification', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'action=markAllRead'
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            document.querySelectorAll('.notification-item.unread')
                .forEach(item => item.classList.remove('unread'));
            updateNotificationCount();
        }
    });
}

// Show toast notification
function showToast(message, type = 'info') {
    const toast = document.getElementById('notificationToast');
    if (toast) {
        const toastBody = toast.querySelector('.toast-body');
        toastBody.textContent = message;
        
        // Add appropriate styling based on type
        toast.className = `toast ${type === 'error' ? 'bg-danger text-white' : 
                                  type === 'success' ? 'bg-success text-white' : 
                                  'bg-white'}`;
        
        const bsToast = new bootstrap.Toast(toast);
        bsToast.show();
    }
}

// Update notification count periodically
document.addEventListener('DOMContentLoaded', function() {
    // Initial count
    updateNotificationCount();
    
    // Update every minute
    setInterval(updateNotificationCount, 60000);
});

// Course form validation
function validateCourseForm() {
    const required = [
        'courseTitle',
        'courseCode',
        'term',
        'schedule',
        'deliveryMethod',
        'outline',
        'compensation',
        'applicationDeadline'
    ];
    
    for (const field of required) {
        const input = document.querySelector(`[name="${field}"]`);
        if (!input || !input.value.trim()) {
            showToast(`Please fill in the ${field.replace(/([A-Z])/g, ' $1').toLowerCase()}`, 'error');
            if (input) input.focus();
            return false;
        }
    }
    return true;
}

// Date validation for course form
function validateCourseDates() {
    const startDate = new Date(document.querySelector('[name="startDate"]').value);
    const endDate = new Date(document.querySelector('[name="endDate"]').value);
    const deadline = new Date(document.querySelector('[name="applicationDeadline"]').value);
    const today = new Date();

    if (deadline < today) {
        alert('Application deadline cannot be in the past');
        return false;
    }

    if (startDate >= endDate) {
        alert('End date must be after start date');
        return false;
    }

    if (deadline > startDate) {
        alert('Application deadline must be before course start date');
        return false;
    }

    return true;
}

// Prevent form double submission
document.addEventListener('DOMContentLoaded', function() {
    const forms = document.querySelectorAll('form');
    forms.forEach(form => {
        form.addEventListener('submit', function() {
            const submitButton = form.querySelector('button[type="submit"]');
            if (submitButton) {
                submitButton.disabled = true;
                submitButton.innerHTML = 'Processing...';
            }
        });
    });
});

function updateCourseStatus(courseId, newStatus) {
    if (confirm('Are you sure you want to ' + 
        (newStatus === 'ACTIVE' ? 'activate' : 'deactivate') + 
        ' this course?')) {
        fetch(`${pageContext.request.contextPath}/course/updateStatus`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `courseId=${courseId}&status=${newStatus}`
        })
        .then(response => {
            if (response.ok) {
                window.location.reload();
            } else {
                alert('Failed to update course status');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('An error occurred while updating the course status');
        });
    }
} 