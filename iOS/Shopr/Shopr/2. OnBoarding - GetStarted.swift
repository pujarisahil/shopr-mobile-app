//
//  2. OnBoarding - GetStarted.swift
//  Shopr
//
//  Created by Sahil Pujari on 10/20/16.
//  Copyright © 2016 Sahil Pujari. All rights reserved.
//
import UIKit

class GetStartedController: UIViewController {
    
    @IBOutlet weak var getStartedButton : UIButton!
    override func viewDidLoad() {
        super.viewDidLoad()
    }
//    
//    override func viewDidAppear(_ animated: Bool) {
//        self.transitionView()
//    }
    
    @IBAction func transitionView(_ sender: UIButton!) {
        let mainStoryboard = UIStoryboard(name: "Main", bundle: Bundle.main)
        let vc : UIViewController = mainStoryboard.instantiateViewController(withIdentifier: "formsMain") as UIViewController
        self.present(vc, animated: true, completion: nil)
    }
    
    func transitionView() {
        let formsController:FormsController = FormsController()
        self.present(formsController, animated: true, completion: nil)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
}
