//
// SecureImage
//
// Copyright © 2017 Province of British Columbia
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at 
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
// Created by Jason Leach on 2017-12-13.
//

import UIKit
import LocalAuthentication

class LockScreenAuthenticateViewController: UIViewController {

    static func viewController() -> LockScreenAuthenticateViewController {
        let storyboard = UIStoryboard(name: String(describing: self), bundle: nil)
        let vc = storyboard.instantiateInitialViewController() as! LockScreenAuthenticateViewController
        
        return vc
    }

    override open var preferredStatusBarStyle: UIStatusBarStyle {
        
        get {
            return Theme.preferredStatusBarStyle
        }
    }

    override func viewDidLoad() {

        super.viewDidLoad()
        
        commonInit()
    }
    
    override func viewDidAppear(_ animated: Bool) {

        super.viewDidAppear(animated)
        
        authenticate()
    }
    
    private func commonInit() {
        
        view.backgroundColor = Theme.governmentDarkBlue
    }

    private func checkAuthenticaitonPolicy() {
        
        let context = LAContext()
        var authError: NSError?
        // The reson comes from the plist iOS 10+. Not sure why its still needed
        // here.
        let reason = "This is used to secure the application and its contents"

        // policy `deviceOwnerAuthentication` should do biometric || passcode depending on how
        // the device is configured.
        if context.canEvaluatePolicy(.deviceOwnerAuthentication, error: &authError) {
            context.evaluatePolicy(.deviceOwnerAuthentication, localizedReason: reason) { success, error in
                if success {
                    DispatchQueue.main.async {
                        NotificationCenter.default.post(Notification(name: .userAuthenticated))
                    }
                } else {
                    // Typically "Canceled by user."
                    print("WARN: Local authentication failed, message = \(String(describing: error?.localizedDescription))")
                }
            }
        } else {
            // Could not evaluate policy; look at authError and present an appropriate message to user
            print("no go")
        }
    }
    
    @IBAction func authenticate() {

        checkAuthenticaitonPolicy()
    }
}
