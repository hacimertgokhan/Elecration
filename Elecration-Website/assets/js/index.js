function download(fileUrl, fileName){
    console.log("Download event just called.");
    const a = document.createElement("a");
    a.href = fileUrl;
    a.setAttribute("download", fileName);
    a.click();
}