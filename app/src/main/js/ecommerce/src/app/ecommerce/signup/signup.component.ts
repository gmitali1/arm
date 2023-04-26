import {Component, OnInit} from '@angular/core';
import {EcommerceService} from "../services/EcommerceService";
import {componentRefresh} from "@angular/core/src/render3/instructions";
import {Observable} from "rxjs";
import {Router} from "@angular/router";

@Component({
    selector: 'signup',
    templateUrl: './signup.component.html',
    styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

    ngOnInit(): void {
        console.log("checking")
        if(this.isLoggedIn == true) {
            this.router.navigate(['/']).then(r => console.log("redirecting..."));
        }
    }

    isLoggedIn: boolean = false;
    username: string = "";
    password: string = "";
    password2: string = "";
    message: string = "";

    constructor(private ecommerceService: EcommerceService, private router: Router) {
    }

    signup() {
        if(this.password != this.password2 && this.password != "") {
            this.message = "passwords dont match. try again";
            return;
        }

        this.ecommerceService.signup(this.username, this.password).then(
            (res) => {
                this.isLoggedIn = true;
                this.router.navigate(['/']).then(r => console.log("redirecting..."));

            }, (error) => {
                this.message = "Signup Failed, please try again with a different username"
            }
        );
    }
}
