import {ProductOrder} from "./product-order.model";

export class Order {
    id: number;
    dateCreated: string;
    status: string;
    orderProducts: ProductOrder[];
    numberOfProducts: number;
    totalOrderPrice: number;


    constructor(id: number, dateCreated: string, status: string,
                orderProducts: ProductOrder[], numberOfProducts: number, totalOrderPrice: number) {
        this.id = id;
        this.dateCreated = dateCreated;
        this.status = status;
        this.orderProducts = orderProducts;
        this.numberOfProducts = numberOfProducts;
        this.totalOrderPrice = totalOrderPrice;
    }
}
