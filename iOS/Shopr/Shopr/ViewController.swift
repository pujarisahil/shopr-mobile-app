//
//  ViewController.swift
//  Shopr
//
//  Created by Sahil Pujari on 10/17/16.
//  Copyright © 2016 Sahil Pujari. All rights reserved.
//

import UIKit

class ViewController: UIViewController {

    @IBOutlet weak var logo : UIImageView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        UIView.animate(withDuration: 1.0, delay: 1.0, options: UIViewAnimationOptions.curveEaseIn, animations: {
                self.logo.alpha = 1.0
            }, completion: nil)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
}
