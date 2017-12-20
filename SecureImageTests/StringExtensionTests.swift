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
// Created by Jason Leach on 2017-12-20.
//

import XCTest

@testable import SecureImage

class StringExtensionTests: XCTestCase {

    func textStringExtensionExists() {
        
        guard let _ = "Hello World".toCammelCase() else {
            XCTAssertTrue(false)
            return
        }
        
        XCTAssertTrue(true)
    }
    
    func testCammelCaseStringResult() {
        
        let testString = "Hello World this is great"
        let expectedValue = "helloWorldThisIsGreat"
        
        guard let result = testString.toCammelCase() else {
            XCTFail()
            return
        }
        
        XCTAssertTrue(result == expectedValue)
    }
}
