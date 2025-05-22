document.addEventListener('DOMContentLoaded', function() {
    const sortSelect = document.getElementById('sort-by');
    const productBox = document.querySelector('.product-box');
    
    // Set the initial value of the dropdown based on URL parameter if exists
    const urlParams = new URLSearchParams(window.location.search);
    const sortParam = urlParams.get('sort');
    if (sortParam) {
        sortSelect.value = sortParam;
    }
    
    sortSelect.addEventListener('change', function() {
        sortProducts(this.value);
        
        // Update URL with sort parameter without reloading the page
        const currentUrl = new URL(window.location.href);
        currentUrl.searchParams.set('sort', this.value);
        window.history.pushState({}, '', currentUrl);
    });
    
    function sortProducts(sortType) {
        const productContainers = Array.from(document.querySelectorAll('.product-container'));
        
        productContainers.sort((a, b) => {
            const priceA = parseFloat(a.querySelector('h5').textContent.replace('Rs.', ''));
            const priceB = parseFloat(b.querySelector('h5').textContent.replace('Rs.', ''));
            
            switch(sortType) {
                case 'priceHigh':
                    return priceB - priceA; // High to low
                case 'priceLow':
                    return priceA - priceB; // Low to high
                default:
                    return 0; // Default order (maintain original order)
            }
        });
        
        // Clear existing products
        productBox.innerHTML = '';
        
        // Append sorted products
        productContainers.forEach(container => {
            productBox.appendChild(container);
        });
    }
    
    // Initial sort if parameter exists
    if (sortParam) {
        sortProducts(sortParam);
    }
});