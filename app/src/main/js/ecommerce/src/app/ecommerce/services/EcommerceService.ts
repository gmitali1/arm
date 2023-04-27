import {ProductOrder} from "../models/product-order.model";
import {Subject} from "rxjs/internal/Subject";
import {ProductOrders} from "../models/product-orders.model";
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Injectable} from "@angular/core";

@Injectable()
export class EcommerceService {
    private productsUrl = "/api/products";
    private ordersUrl = "/api/orders";
    private loginUrl = "/api/users";

    private productOrder: ProductOrder;
    private orders: ProductOrders = new ProductOrders();

    private productOrderSubject = new Subject();
    private ordersSubject = new Subject();
    private totalSubject = new Subject();

    private total: number;

    ProductOrderChanged = this.productOrderSubject.asObservable();
    OrdersChanged = this.ordersSubject.asObservable();
    TotalChanged = this.totalSubject.asObservable();

    constructor(private http: HttpClient) {
    }

    getAllProducts() {
        console.log("Getting Products");
        return this.http.get(this.productsUrl);
    }

    getAllOrders() {
        const userId = localStorage.getItem('userId');
        console.log('User id of the logged in user is ' + userId)
        return this.http.get(this.ordersUrl + '?userId=' + userId);
    }

    saveOrder(order: ProductOrders) {
        const userId = localStorage.getItem('userId');
        return this.http.post(this.ordersUrl + '?userId=' + userId, order);
    }

    login(username : string , password : string) : Promise<string> {
        return new Promise((resolve, reject) => {
            const params = { username: username, password: password };
            this.http.get(
                this.loginUrl, { params }
            ).subscribe((data: HttpResponse<any>) => {
                resolve("OK");
                console.log('User id = ' + data['id']);
                localStorage.setItem('userId', data['id']);
            }, (error) => {
                reject("NOT_OK");
            });
        });
    }

    set SelectedProductOrder(value: ProductOrder) {
        this.productOrder = value;
        this.productOrderSubject.next();
    }

    get SelectedProductOrder() {
        return this.productOrder;
    }

    set ProductOrders(value: ProductOrders) {
        this.orders = value;
        this.ordersSubject.next();
    }

    get ProductOrders() {
        return this.orders;
    }

    get Total() {
        return this.total;
    }

    set Total(value: number) {
        this.total = value;
        this.totalSubject.next();
    }
}
