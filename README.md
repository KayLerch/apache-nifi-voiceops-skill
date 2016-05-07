# apache-nifi-voiceops-skill
This one is an Alexa skill which can be used to control an Apache NiFi dataflow. 

# Demo
See https://www.youtube.com/watch?v=s5b_yjLVkrk

# Getting Started
1. Create an Alexa Skill over the Developer Console
2. Use intent-schema you can find in the resources folder (you need to create some custom slot types whose value enumerations also contained in the resources)
3. Use utterance you can find in the resources folder
4. Configure values in the app.properites file
5. Maven package and upload jar with dependencies as a Lambda function
6. Define "Alexa Skill Kit" as the Lambda's event source
7. Set the ARN of this Lambda function as your skill's endpoint

Of course you need a NiFi server and a data-flow you want to control.
This sample project is just a demonstration of how you can develop your own VoiceOps service for NiFi.