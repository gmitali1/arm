import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';

import {AppComponent} from './app.component';
import {EcommerceComponent} from './ecommerce/ecommerce.component';
import {ProductsComponent} from './ecommerce/products/products.component';
import {ShoppingCartComponent} from './ecommerce/shopping-cart/shopping-cart.component';
import {OrdersComponent} from './ecommerce/orders/orders.component';
import {EcommerceService} from "./ecommerce/services/EcommerceService";
import {DisplayOrdersComponent} from "./ecommerce/display-orders/display-orders.component";
import {RouterModule, Routes} from "@angular/router";
import {NavbarComponent} from "./ecommerce/navbar/navbar.component";
import {LoginComponent} from "./ecommerce/login/login.component";
import {SignupComponent} from "./ecommerce/signup/signup.component";
import {HashLocationStrategy, LocationStrategy} from '@angular/common'; // <-- import LocationStrategy and HashLocationStrategy


const routes: Routes = [
    {path: '', component: EcommerceComponent},
    {path: 'orders', component: DisplayOrdersComponent},
    {path: 'login', component: LoginComponent},
    {path: 'signup', component: SignupComponent}
];

@NgModule({
    declarations: [
        AppComponent,
        EcommerceComponent,
        ProductsComponent,
        ShoppingCartComponent,
        OrdersComponent,
        DisplayOrdersComponent,
        NavbarComponent,
        LoginComponent,
        SignupComponent
    ],
    imports: [
        BrowserModule,
        HttpClientModule,
        FormsModule,
        ReactiveFormsModule,
        RouterModule.forRoot(routes)
    ],
    exports: [
        RouterModule
    ],
    providers: [EcommerceService,
        {provide: LocationStrategy, useClass: HashLocationStrategy}
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}

