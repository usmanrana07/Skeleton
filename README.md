# Skeleton
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)  
The library provides Shimmer animation effect in RecyclerView Adapters and Views.

# Preview


![img](screenshots/01.gif)
![img](screenshots/02.gif)
![img](screenshots/03.gif)
![img](screenshots/04.gif)

    
    

# Usage
  For RecyclerView:
  ```java
  skeletonScreen = Skeleton.bind(recyclerView)
                                .adapter(adapter)
                                .load(R.layout.item_skeleton_news)
                                .show();
  ``` 
       
                                
                         
  For View:
  ```java
  skeletonScreen = Skeleton.bind(rootView)
                                .load(R.layout.layout_img_skeleton)
                                .show();
  ```    
       
                                
                       
  More Config:
  ```java
  .shimmer(true)      // whether show shimmer animation.                      default is true
  .count(10)          // the recycler view item count.                        default is 10
  .color(color)       // the shimmer color.                                   default is #a2878787
  .duration(1000)     // the shimmer animation duration.                      default is 1000;
  .frozen(false)      // whether frozen recyclerView during skeleton showing  default is true; 
```
                            
  when data return you can call the method to hide skeleton loading view 
   ```java
  skeletonScreen.hide()
   ```
       
        
 # Thanks
 
 https://github.com/team-supercharge/ShimmerLayout
 
