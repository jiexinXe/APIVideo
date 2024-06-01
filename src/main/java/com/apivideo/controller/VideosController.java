package com.apivideo.controller;

import com.apivideo.entity.Videos;
import com.apivideo.service.VideosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import com.apivideo.handler.NonStaticResourceHttpRequestHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/videos")
public class VideosController {
    //引入返回视频流的组件
    private final NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler;
    @Autowired
    private VideosService videosService;

    public VideosController(NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler) {
        this.nonStaticResourceHttpRequestHandler = nonStaticResourceHttpRequestHandler;
    }

    //获取所有视频信息
    @GetMapping("/list")
    public List<Videos> getAllVideos() {
        return videosService.list();
    }

    //按video_id查找单个视频
    @GetMapping("/get/{id}")
    public void getVideoById(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) throws ServletException, IOException {
        Videos video = videosService.getById(id);
        //从视频信息中单独把视频路径信息拿出来保存
        String videoPathUrl=video.getVideoPath();
        //保存视频磁盘路径
        Path filePath = Paths.get("src/main/resources/videos/" + videoPathUrl);
        //Files.exists：用来测试路径文件是否存在
        if (Files.exists(filePath)) {
            //获取视频的类型，比如是MP4这样
            String mimeType = Files.probeContentType(filePath);
            if (StringUtils.hasText(mimeType)) {
                //判断类型，根据不同的类型文件来处理对应的数据
                response.setContentType(mimeType);
            }
            //转换视频流部分
            request.setAttribute(NonStaticResourceHttpRequestHandler.ATTR_FILE, filePath);
            nonStaticResourceHttpRequestHandler.handleRequest(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        }
    }

    //保存上传的视频
    @PostMapping(value = "/saveVideo")
    @ResponseBody
    //Map<String,String>: map是键值对形式组成的集合，类似前端的数组但是里面是键值对形式的，前后两个string代表键和值都是字符串格式的。
    //post请求传入的参数：MultipartFile file(理解为springmvc框架给我们提供的工具类，代表视频流数据)，SavePath(前台传来的地址路径，也是用来后端保存在服务器哪个文件夹的地址)
    public Map<String,String> savaVideoTest(@RequestParam("file") MultipartFile file, @RequestParam String SavePath) throws IllegalStateException {
        //new一个map集合出来
        Map<String,String> resultMap = new HashMap<>();
        try{
            //获取文件后缀，因此此后端代码可接收一切文件，上传格式前端限定
            String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
            if(!fileExt.equals("mp4")){
                resultMap.put("不支持的视频类型","400");
                return resultMap;
            }
            //保存视频的原始名字
            String videoNameText = file.getOriginalFilename();
            //保存视频url路径地址
            String videoUrl = SavePath + "/" + videoNameText;
            //要保存的视频类
            Videos saveVideo = new Videos();
            saveVideo.setDescription(videoNameText);
            saveVideo.setVideoPath(videoUrl);
            videosService.save(saveVideo);
            //判断SavePath这个路径也就是需要保存视频的文件夹是否存在
            File filepath = new File(SavePath, file.getOriginalFilename());
            if (!filepath.getParentFile().exists()) {
                //如果不存在，就创建一个这个路径的文件夹。
                filepath.getParentFile().mkdirs();
            }
            //保存视频：把视频按照前端传来的地址保存进去，还有视频的名字用唯一标识符显示，需要其他的名字可改这
            File fileSave = new File(SavePath, videoNameText);
            //下载视频到文件夹中
            file.transferTo(fileSave);
            //构造Map将视频信息返回给前端
            //视频名称重构后的名称：这里put代表添加进map集合内，和前端的push一样。括号内是前面字符串是键，后面是值
            resultMap.put("newVideoName",videoNameText);
            //正确保存视频成功，则设置返回码为200
            resultMap.put("resCode","200");
            //返回视频保存路径
            resultMap.put("VideoUrl",videoUrl);
            //到这里全部保存好了，把整个map集合返给前端
            return  resultMap;

        }catch (Exception e){
            //在命令行打印异常信息在程序中出错的位置及原因
            e.printStackTrace();
            //返回有关异常的详细描述性消息。
            e.getMessage();
            //保存视频错误则设置返回码为400
            resultMap.put("resCode","400");
            //这时候错误了，map里面就添加了错误的状态码400并返回给前端看
            return  resultMap ;
        }
    }

    //按video_id删除视频
    @DeleteMapping("/deleteVideo/{id}")
    public ResponseEntity<Object> deleteVideoById(@PathVariable Integer id) {
        boolean state = videosService.removeById(id);
        return ResponseEntity.ok(state);
    }


    //查询当前推荐视频
    @GetMapping("/recommend")
    public List<Videos> getRecommendedVideos() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = null;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        }
        return videosService.getRecommendedVideos(username, 4);
    }

    // 点赞，URL为http://localhost:8080/videos/{videoId}/like?userId={userId}
    // 示例：http://localhost:8080/videos/172/like?userId=29
    @PostMapping("/{videoId}/like")
    public String likeVideo(@PathVariable Integer videoId, @RequestParam Integer userId) {
        videosService.likeVideo(userId, videoId);
        return "Video liked successfully!";
    }
}
