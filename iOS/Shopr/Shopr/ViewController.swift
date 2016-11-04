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
    }
    
    override func viewDidAppear(_ animated: Bool) {
        UIView.animate(withDuration: 1.3, delay: 1.3, options: UIViewAnimationOptions.curveEaseIn, animations: {
            self.logo.alpha = 1.0
            }, completion: {finished in
                self.transition()
            })
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func transition() {
        let onBoardingPageViewController:OnBoardingPageViewController = OnBoardingPageViewController()
        self.present(onBoardingPageViewController, animated: true, completion: nil)
    }
}
