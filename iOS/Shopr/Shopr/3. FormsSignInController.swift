//
//  3. FormsSignInController.swift
//  Shopr
//
//  Created by Sahil Pujari on 10/19/16.
//  Copyright © 2016 Sahil Pujari. All rights reserved.
//

import Foundation
import UIKit
import CryptoSwift
import SwiftHTTP
import Alamofire

class FormsSignInController: UIViewController {
    
    @IBOutlet weak var getStartedButton: UIButton!
    
    @IBOutlet weak var usernameField: UITextField!
    @IBOutlet weak var passwordField: UITextField!
    @IBOutlet weak var warningMessage: UITextView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(FormsSignInController.dismissKeyboard))
        
        //Uncomment the line below if you want the tap not not interfere and cancel other interactions.
        //tap.cancelsTouchesInView = false
        usernameField.autocorrectionType = .no
        warningMessage.isEditable = false
        view.addGestureRecognizer(tap)

    }
    
    @IBAction func transitionView(sender : UIButton!) {
        let viewController:ViewController = ViewController()
        self.present(viewController, animated: true, completion: nil)
    }
    
    @IBAction func signIn(sender : UIButton!) {
        warningMessage.text = ""
        if usernameField.text == "" || passwordField.text == "" {
            warningMessage.text = "Username or Password was empty"
        } else {
            let hashedPassword = passwordField.text?.sha512()
            let username = usernameField.text
            let params = ["email": username, "password": hashedPassword]
            
            Alamofire.request("http://api.shopr.store/accounts/login", method: .post, parameters: params, encoding: JSONEncoding.default).responseJSON { response in
                    let statusCode = response.response?.statusCode
                
                    if statusCode == 200 {
                        let mainStoryboard = UIStoryboard(name: "Main", bundle: Bundle.main)
                        let vc : UIViewController = mainStoryboard.instantiateViewController(withIdentifier: "mainPage") as UIViewController
                        self.present(vc, animated: true, completion: nil)
                        
                    } else if statusCode == 422 {
                        self.warningMessage.text = "Login failed"
                    } else {
                        self.warningMessage.text = "Server Error"
                    }
                }
            }
    }
    
    @IBAction func signUp(sender : UIButton!) {
        let mainStoryboard = UIStoryboard(name: "Main", bundle: Bundle.main)
        let vc : UIViewController = mainStoryboard.instantiateViewController(withIdentifier: "signUpForm") as UIViewController
        self.present(vc, animated: true, completion: nil)
    }
    
    func dismissKeyboard() {
        view.endEditing(true)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
}
