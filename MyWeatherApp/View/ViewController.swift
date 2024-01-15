//
//  ViewController.swift
//  MyWeatherApp
//
//  Created by Andrii Melnyk on 1/12/24.
//

import UIKit

import CoreLocation

class ViewController: UIViewController, CLLocationManagerDelegate, UITextFieldDelegate {
    
    @IBOutlet weak var searchTextField: UITextField!
    
    var myLocation = CLLocationManager()
    
    override func viewDidLoad() {
        
        searchTextField.delegate = self
        myLocation.delegate = self
        myLocation.requestAlwaysAuthorization()
        myLocation.requestWhenInUseAuthorization()
        myLocation.desiredAccuracy = kCLLocationAccuracyNearestTenMeters
        myLocation.startUpdatingLocation()
        myLocation.requestLocation()
        
       
       // createWeatherUrl()

        super.viewDidLoad()
    }

    // MARK: - MyLocation methods
    @IBAction func myLocationButtonPressed(_ sender: UIButton) {
        
        print("MyLocationButton pressed !!! ")
        DispatchQueue.main.async {
            
            self.myLocation.requestLocation()
            fetchTheWeatherForMe(lat: self.myLocation.location!.coordinate.latitude, lon: self.myLocation.location!.coordinate.longitude)
        }
        print (myLocation.location!.coordinate.latitude)
        print(myLocation.location!.coordinate.longitude)
        
        
    }
 
        func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
            let locValue:CLLocationCoordinate2D = manager.location!.coordinate
            print("locations = \(locValue.latitude) \(locValue.longitude)")
        }
    
    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        print("We have some problems with Internet connection \(error)")
    }
    
    // MARK: - UITextField delegates method
    
    
    @IBAction func searchButtonPressed(_ sender: UIButton) {
        
        searchTextField.endEditing(true)
        print("SearchButtonPressed")
    }

    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        print("Log1")
        searchTextField.endEditing(true)
        return true
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        
        if let city = searchTextField.text {
            createWeatherUrl(withString: city)
        }
        
        searchTextField.text = ""
    }
    
}

