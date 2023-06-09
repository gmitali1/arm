import {Component, OnInit} from '@angular/core';
import {EcommerceService} from "../services/EcommerceService";
import {Router} from "@angular/router";

@Component({
    selector: 'login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

    ngOnInit(): void {
        console.log("checking")
        if(this.isLoggedIn == true) {
            this.router.navigate(['/']).then(r => console.log("redirecting..."));
        }
    }

    isLoggedIn: boolean = false;
    username: string = "";
    password: string = "";
    message: string = "";

    constructor(private ecommerceService: EcommerceService, private router: Router) {
    }

    login() {
        this.ecommerceService.login(this.username, this.password).then(
            (res) => {
                if(res == "OK") {
                    this.isLoggedIn = true;
                    this.router.navigate(['/']).then(r => console.log("Redirecting to home page..."));
                } else {
                    this.isLoggedIn = false;
                    this.message = "Login Failed";
                }
            }
        );
    }
}
