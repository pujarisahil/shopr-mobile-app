//
//  3. FormsController.swift
//  Shopr
//
//  Created by Sahil Pujari on 10/19/16.
//  Copyright © 2016 Sahil Pujari. All rights reserved.
//

import Foundation
import UIKit

class FormsController: UIViewController {
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    @IBAction func transitionView(sender : UIButton!) {
        let viewController:ViewController = ViewController()
        self.present(viewController, animated: true, completion: nil)
    }
    
    @IBAction func signUp(sender : UIButton!) {
        let mainStoryboard = UIStoryboard(name: "Main", bundle: Bundle.main)
        let vc : UIViewController = mainStoryboard.instantiateViewController(withIdentifier: "signUpForm") as UIViewController
        self.present(vc, animated: true, completion: nil)

    }
    
    func facebookLogin() {
        
    }
    
    @IBAction func signIn(sender : UIButton!) {
        let mainStoryboard = UIStoryboard(name: "Main", bundle: Bundle.main)
        let vc : UIViewController = mainStoryboard.instantiateViewController(withIdentifier: "signInForm") as UIViewController
        self.present(vc, animated: true, completion: nil)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
}
