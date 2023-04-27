import {Component} from '@angular/core';

@Component({
    selector: 'navbar',
    templateUrl: './navbar.component.html',
    styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
    private collapsed: boolean;
    private orderFinished: boolean;
    private productsC: any;
    private shoppingCartC: any;
    private ordersC: any;

    toggleCollapsed(): void {
        this.collapsed = !this.collapsed;
    }

    reset() {
        this.orderFinished = false;
        this.productsC.reset();
        this.shoppingCartC.reset();
        this.ordersC.paid = false;
    }


    navigateToOrders() {

    }

    performLogout() {
        localStorage.setItem('userId', undefined)
        console.log("Logout Successful")
    }

}
