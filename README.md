# GPA4U

Details of features completed & General state of the app 
------------------------------------------------------------------------------
All features of Sprint 2 have been completed which are GPA pie chart & bar charts in MyGPA screen, showing/hiding expected GPA with switch in Settings screen, credits switch showing/hiding credits shown in MyCourses screen, push notifications switch to receive notification when receiving below 3.00 GPA, switch for disabling all push notifications, switch for receiving notification when receiving below 3.00 expected GPA, push notification popping up after adding and editing assessments only if GPA or expected GPA is below 3.00. If both GPA and expected GPA are below 3.00, two push notifications pop up consecutively.
editing courses & assignments, Letter or S/U grading system, specifying course credits, S/U grading system doesn't affect overall GPA, deleting courses & assignments.

General state of the app is that when a person's GPA or expected changes, push notifications pop up consecutively for each GPA and expected GPA. Also, MyGPA screen shows pie chart and bar chart showing current courses with grades. MyScreen shows switches for disabling/enabling push notifications and hiding/showing expected GPA and expected GPA checkbox in MyAssessment screen.

I implemented MyGPA and Settings. MyGPA has pie chart and bar chart showing course names and grades for each in different random color. I also implemented so that those grades in MyGPA screen include expected grades. I implemented push notifications to pop up when GPA or expected GPA is below 3.00 after adding or editing assessments or courses. I implemented so that both push notifications (GPA and expected GPA) pop up one seconds after another.

## Completed Features
- My GPA
  - Statistics graph to show current GPA
- Settings
  - Grade
    - expected GPA switch to show or hide `expected GPA shown in MyCourses screen`
    - credits switch to show or hide `credits shown in MyCourses screen`
  - Push notifications
    - Switch to receive notification if receiving below 3.00 GPA 
    - Switch called All Notifications to disable all
    - Switch to receive notification if receiving below 3.00 expected GPA 
- Push Notifications
  - When GPA is below 3.00, push notification pops up after `adding and editing assessments`.
  - When expected GPA is below 3.00, push notification pops up after `adding and editing assessments` seconds after GPA notification pops up.
  

Details of features completed & General state of the app 
------------------------------------------------------------------------------
All features of Sprint 1 have been completed including auto calculating GPA, expected GPA, adding courses & assignments,
editing courses & assignments, Letter or S/U grading system, specifying course credits, S/U grading system doesn't affect overall GPA, deleting courses & assignments. 

General state of the app is that the app is able to add, edit, delete course and assignments and store them to local database. The app calculates overall GPA and expected GPA. Those GPAs change whenever new courses or assignments are added. Those GPAs do not calculate grade from courses with S/U grading system.

I implemented My Course, My Assessment, Add Assessment, and Add Course activities. I put the course, weight, and assessment information into local database and load them when app starts. I also implemented Overall GPA and Overall Expected GPA. Courses with S/U grading system do not affect Overall GPA and Overall Expected GPA. Those courses will only show S or U instead of a letter grade. Multiple weights on a single course will show those weight names on Add Assessment activity.

## Completed Features 
- My Courses
  - MyCourse Activity
  - floating button to show Add Course activity
  - Tab bar to show My Courses, My Assignments, and Settings activities - Highlight current tab activity showing
  - Shows lists of courses
- Add Course
  - Add Course Activity
  - Add button to add more categories for assignments - Create drop downs for credits, grading system.
  - Save button
  - Back button for app bar.
- My Assessments
  - My Assessment Activity
  - Add button to add assessment - Show lists of assessment
  - Shows grades for each assessment
- Add Assessment
  - Drop downs for assessment type
  - Drop down for expected grade for assessment
  - Back button for app bar.
