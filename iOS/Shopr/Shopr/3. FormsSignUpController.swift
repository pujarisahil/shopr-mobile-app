//
//  3. FormsSignUpController.swift
//  Shopr
//
//  Created by Sahil Pujari on 10/19/16.
//  Copyright © 2016 Sahil Pujari. All rights reserved.
//

import Foundation

import UIKit

class FormsSignUpController: UIViewController {
    
    @IBOutlet weak var getStartedButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    @IBAction func transitionView(sender : UIButton!) {
        let viewController:ViewController = ViewController()
        self.present(viewController, animated: true, completion: nil)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
}
