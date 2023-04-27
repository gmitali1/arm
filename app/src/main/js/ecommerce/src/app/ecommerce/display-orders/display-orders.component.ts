import {Component, OnInit} from '@angular/core';
import {EcommerceService} from "../services/EcommerceService";
import {Order} from "../models/order.model";

@Component({
    selector: 'display-orders',
    templateUrl: './display-orders.component.html',
    styleUrls: ['./display-orders.component.css']
})
export class DisplayOrdersComponent implements OnInit{
    orders: Order[] = [];


    constructor(private ecommerceService: EcommerceService) {
    }

    ngOnInit(): void {
        this.ecommerceService.getAllOrders().subscribe( (orders_ : any[]) => {
            console.log("ayo")
            orders_.forEach(ord => {
                this.orders.push(new Order(
                    ord.id,
                    ord.dataCreated,
                    ord.status,
                    ord.orderProducts,
                    ord.numberOfProducts,
                    ord.totalOrderPrice
                ))
            })
        });
    }

}
