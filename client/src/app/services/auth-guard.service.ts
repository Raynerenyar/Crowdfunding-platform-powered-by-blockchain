import { CanActivateFn, Router } from "@angular/router";
import { SessionStorageService } from "./session.storage.service";
import { inject } from "@angular/core";

export function authenticationGuard(): CanActivateFn {
    return () => {
        const storageSvc: SessionStorageService = inject(SessionStorageService)
        const router: Router = inject(Router)

        if (storageSvc.isLoggedIn()) {
            return true
        }
        // TODO: redirect to login
        // storageSvc.login();
        router.navigate(['explore']).then(() => window.location.reload())
        return false
    };
}