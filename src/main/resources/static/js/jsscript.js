$(document).ready(function() {
    // change quantity of item when click + - button
    $('.input-counter__plus').on('click', function(){
        var $input = $(this).parent().find('.input-counter__text');
        var count = parseInt($input.val()) + 1; 
        $input.val(count).change();
    });
    $('.input-counter__minus').on('click', function(){
        var $input = $(this).parent().find('.input-counter__text');
        var count = parseInt($input.val()) - 1; 
        $input.val(count).change();
    })
    // change quantity if its > max or < min
    $('.input-counter__text').on('change', function(){
        var $this = $(this);
        var min = $this.data('min');
        var max = $this.data('max');
        var val = parseInt($this.val());// Current value
        // Restrictions check
        if (!val) {
            val = 1;
        }
        val = Math.min(val,max);
        val = Math.max(val,min);
        $this.val(val);
    });
    //select box city on change
    $('#select-box-city').on('change', function(){
        var id = $(this).children(":selected").attr("value");
        var name =  $(this).children(":selected").text();
        $('#input-province-name').val(name);
        initSelectBoxDistrict(id);
        $('#select-box-ward').find('option').remove();
        $('#select-box-ward').append($("<option disabled selected value> -- Chọn phường/xã -- </option>"));
    });
    //select box districy on change
    $('#select-box-district').on('change', function(){
        var district_id = $(this).children(":selected").attr("value");
        var name = $(this).children(":selected").text()
        $('#input-district-name').val(name);
        initSelectBoxWard(district_id)
    });
    $('#select-box-ward').on('change', function(){
        var district_id =$('#select-box-district').children(":selected").attr("value");
        var ward_code = $(this).find(":selected").val();
        var ward_name = $(this).find(":selected").text();
        $('#input-ward-name').val(ward_name);
        console.log($('#input-province-name').val() + ' province');
        console.log($('#input-district-name').val() + ' district');
        console.log($('#input-ward-name').val() + ' ward');
        if($('#ship-fee').length) // check element exist
            calculateShipFee(district_id, ward_code);
        if($('#delivery-time').length)
            getDeliveryTime(district_id, ward_code);
    })
});

function updateTotalPrice(ele){
    var $this = $(ele);
    var min = $this.data('min');
    var max = $this.data('max');
    var val = parseInt($this.val());// Current value
    // Restrictions check
    if (!val) {
        val = 1;
    }
    val = Math.min(val,max);
    val = Math.max(val,min);
    $this.val(val);
    //loop through table and get price, quantity of each product
    var totalItem = 0;
    var totalPrice = 0;
    $('#tbl-product-cart > tbody >tr').each(function (i, row){
        var price = $(row).children('td').eq(1).children('span');
        var quantity = $(row).children('td').eq(2).children('div').eq(1).children('div').eq(0).children('.quantity_input').eq(0);
        totalItem += +quantity.val();
        totalPrice += parseFloat(quantity.val()) * parseFloat(price.text().replace(' ₫',' ').replaceAll(',',''));
        price.text(numberWithCommas(price.text().replace('₫','').replaceAll(',',''))+' ₫');
    });
    $('#total-price').text(numberWithCommas(totalPrice) + ' ₫');
    $('#totalItem').text(totalItem);
}
//remove item form cart when click delete icon
function removeItemFromCart(element){
    var sku_id = $(element).parent().find('.sku_id').val();
    console.log(sku_id);
    $('#hide-sku-id').val(sku_id);
    $('#form-remove-item').submit();
}

function numberWithCommas(x) {
    var parts = x.toString().split(".");
    parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ".");
    return parts.join(".");
}
function areEqual(array1, array2) {
    return array1.every(element => {
    if (array2.includes(element.variant_value)) {
        return true;
    }
        return false;
    });
}
// create option for select box city name
function initSelectBoxCity(cityName, districtName, wardName){
    var url ="https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/province";
    var element = $('#select-box-city');
    $.ajax({
        url :  url,
        type: 'GET',
        headers :{
            token: "201937cd-db8b-11ed-ab31-3eeb4194879e"
        },
        success: function(res) {
            for(var i =0;i<res.data.length;i++){
                if(res.data[i].ProvinceName != cityName){
                    element.append($("<option></option>").text(res.data[i].ProvinceName).attr('value',res.data[i].ProvinceID ))
                }
                else {
                    element.append($("<option></option>").text(res.data[i].ProvinceName).attr('value',res.data[i].ProvinceID ).attr('selected','selected'));
                    initSelectBoxDistrict(res.data[i].ProvinceID, districtName, wardName);
                }
                    
            }
        },
        error: function(xhr,status, errorThrown) {
            console.log("failed");
            console.log(status);
            console.log(errorThrown);
        }
    })
}
// create option for select box district name
function initSelectBoxDistrict(province_id, districtName, wardName){
    var url ="https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/district";
    var element = $('#select-box-district');
    $.ajax({
        url :  url,
        type: 'GET',
        headers :{
            token: "201937cd-db8b-11ed-ab31-3eeb4194879e"
        },
        data :{
            province_id : province_id
        },
        success: function(res) {
            element.find('option').remove();
            element.append($("<option disabled selected value> -- Chọn quận/huyện -- </option>"));
            for(var i =0;i<res.data.length;i++){
                if(res.data[i].DistrictName != districtName)
                    element.append($("<option></option>").text(res.data[i].DistrictName).attr('value',res.data[i].DistrictID ))
                else{
                    element.append($("<option></option>").text(res.data[i].DistrictName).attr('value',res.data[i].DistrictID ).attr('selected','selected'));
                    initSelectBoxWard(res.data[i].DistrictID,  wardName);
                }    
            }
        },
        error: function(xhr,status, errorThrown) {
            console.log("failed");
            console.log(status);
            console.log(errorThrown);
        }
    })
}
// create option for select box ward
function initSelectBoxWard(district_id, wardName){
    var url ="https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/ward";
    var element = $('#select-box-ward');
    $.ajax({
        url :  url,
        type: 'GET',
        headers :{
            token: "201937cd-db8b-11ed-ab31-3eeb4194879e"
        },
        data :{
            district_id : district_id
        },
        success: function(res) {
            element.find('option').remove();
            element.append($("<option disabled selected value> -- Chọn phường/xã -- </option>"));
            for(var i =0;i<res.data.length;i++){
                if(res.data[i].WardName != wardName)
                    element.append($("<option></option>").text(res.data[i].WardName).attr('value',res.data[i].WardCode ))
                else 
                    element.append($("<option></option>").text(res.data[i].WardName).attr('value',res.data[i].WardCode ).attr('selected','selected'));
            }
        },
        error: function(xhr,status, errorThrown) {
            console.log(xhr);
            console.log(status);
            console.log(errorThrown);
        }
    })
}
//tinh phi giao hang
function calculateShipFee(to_district_id, to_ward_code){
    var url ="https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee";
    $.ajax({
        url :  url,
        type: 'GET',
        headers :{
            token: "201937cd-db8b-11ed-ab31-3eeb4194879e",
            ShopID: "123933"
        },
        data :{
            from_district_id: 1490, //id quan hoang mai
            service_id: 53320,
            to_district_id: to_district_id,
            to_ward_code: to_ward_code,
            height: 20,
            length: 20,
            width: 20,
            weight: 1000,
            insurance_value: 3000000
        },
        success: function(res) {
            var ship_fee = res.data.total ;
            $('#ship-fee').text(numberWithCommas(ship_fee)+ ' ₫');
            console.log('phi ship '+ship_fee)
            initOrderTotalPrice(ship_fee)
        },
        error: function(xhr,status) {
            console.log(xhr);
            console.log(status);
        }
    })
}

//get the expected delivery time
 function getDeliveryTime(to_district_id,to_ward_code){
    var url ="https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/leadtime";
    $.ajax({
        url :  url,
        type: 'GET',
        headers :{
            token: "201937cd-db8b-11ed-ab31-3eeb4194879e",
            ShopID: "123933"
        },
        data :{
            from_district_id: 1490, //id quan hoang mai
            service_id: 53320,
            to_district_id: to_district_id,
            to_ward_code: to_ward_code
        },
        success: function(res) {
            var milisecond = res.data.leadtime *1000;
            console.log(new Date(milisecond))
            $('#delivery-time').text(convertDate(milisecond));
        },
        error: function(xhr,status) {
            console.log(xhr);
            console.log(status);
        }
    })
 }

//convert date
function convertDate(date_input){
    var date = new Date(date_input);
    var day_of_week = date.getDay();
    switch(day_of_week){
        case 0:
            day_of_week = 'Chủ nhật';
            break;
        case 1:
            day_of_week = 'Thứ Hai';
            break;
        case 2:
            day_of_week = 'Thứ Ba';
            break;
        case 3:
            day_of_week = 'Thứ Tư';
            break;
        case 4:
            day_of_week = 'Thứ Năm';
            break;
        case 5:
            day_of_week = 'Thứ Sáu';
            break;
        case 6:
            day_of_week = 'Thứ Bảy';
            break;
    }
    var day_of_month = date.getDate();
    var month = date.getMonth() + 1;
    var year = date.getFullYear();
    return day_of_week + ' '+ day_of_month + '/' + month + '/' + year;
}

//order.html
//calculate price of product, shipfee then show on UI
function initOrderTotalPrice(ship_fee){
    if(ship_fee == null) ship_fee = 0;
    var product_total_price = 0;
    var order_total_price = 0;
    //loop through list product
    $('.o-card').each(function(i, element){
        var price = $(this).find('.o-card__price').text().replaceAll('.','').replace(' ₫','');
        var quantity = $(this).find('.o-card__quantity2').text().replaceAll('x','').replace(' ','');
        product_total_price += parseFloat(price)*parseFloat(quantity);
    })
    $('#total-product-price').text(numberWithCommas(product_total_price)+ ' ₫');
    order_total_price = parseFloat(product_total_price) + +parseFloat(ship_fee);
    $('#total-order-price').text(numberWithCommas(order_total_price)+ ' ₫');
}


function cartNotify(){
    //show notification base on url param
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    if(urlParams.has('add')){
        if(urlParams.get('add') == 'success'){
            $('.alert-success').text('Đã thêm sản phẩm vào giỏ hàng');
            $('.alert-success').show();
        }else{
            $('.alert-danger').text('Thêm sản phẩm vào giỏ hàng thất bại');
            $('.alert-danger').show();
        }
    }else if(urlParams.has('update') || urlParams.has('remove')){
        if(urlParams.get('update') == 'success' || urlParams.get('remove') == 'success'){
            $('.alert-success').text('Cập nhật giỏ hàng thành công');
            $('.alert-success').show();
        }else{
            $('.alert-danger').text('Cập nhật giỏ hàng thất bại');
            $('.alert-danger').show();
        }
    }
    window.setTimeout(function() {
        $(".alert.alert-success, .alert.alert-danger").fadeTo(500, 0).slideUp(500, function(){
            $(this).remove(); 
        });
    }, 5000);
}
class VariantValue{
    constructor(variant, value){
        this.variant = variant;
        this.value = value;
    }
}
function initStatusTimeline(order){
    var order_status = order.status;
    switch (order_status) {
        case 'UNPAID':
            $("#status-placed").addClass("timeline-l-i--finish");
            break;
        case 'PROCESSING':
            $("#status-placed,#status-processing").addClass("timeline-l-i--finish");
            break;
        case 'CONFIRMED':
            $("#status-placed,#status-processing,#status-confirmed").addClass("timeline-l-i--finish");
            break;
        case 'SHIPPING':
            $("#status-placed,#status-processing,#status-confirmed,#status-shipping").addClass("timeline-l-i--finish");
            break;
        case 'SHIPPED':
            $("#status-placed,#status-processing,#status-confirmed,#status-shipping,#status-shipped").addClass("timeline-l-i--finish");
            break;
        case 'SHIPMENT_FAILED':
            $("#status-placed,#status-processing,#status-confirmed,#status-shipping,#status-shipped").addClass("timeline-l-i--finish");
            $('#status-shipped').siblings('.timeline-text').text("Giao hàng thất bại").css({'color':'red'})
            break;
    }
    if(order_status == 'CANCELED' || order_status == 'REJECTED'){
        $("#status-placed").addClass("timeline-l-i--finish");
        if(order.isPaid == 0)
            $("#status-processing").parent().parent().remove();
        else
            $("#status-processing").addClass("timeline-l-i--finish");
        if(order.confirm_order_date == null)
            $("#status-confirmed").parent().parent().remove();
        else
            $('#status-confirmed').addClass("timeline-l-i--finish");
        if(order.confirm_shipping_date == null)
            $("#status-shipping").parent().parent().remove();
        else
            $('#status-shipping').addClass("timeline-l-i--finish");
        $('#status-shipped').addClass("timeline-l-i--finish");
        if(order_status == 'CANCELED')
            $('#status-shipped').siblings('.timeline-text').text("Đơn hàng đã hủy").css({'color':'red'})
        else if(order_status == 'REJECTED')
            $('#status-shipped').siblings('.timeline-text').text("Shop hủy đơn").css({'color':'red'})
    }
}

//product-create.html
$('#btn-submit').on('click', function(e){
    //check input file thumbnail
     e.preventDefault();
    if($.trim($("#thumbnail-add-btn").val()).length == 0 ){
        document.querySelector('body').scrollIntoView({
            behavior: 'smooth'
        });
         $(".thumbnail-add-btn").children('span').text("Vui lòng chọn ảnh bìa")
    }else if($('#img-box').children().length == 0){
        document.querySelector('body').scrollIntoView({
            behavior: 'smooth'
        });
    }
    else {
        var nameArr= [];
        var stop = false;
        $('.var-box').each(function(){
            var var_name = $(this).find('.var-name-input').val();
            if(var_name != '' && var_name != ' ') nameArr.push(var_name);
            $(this).find('.opt-wrap').each(function(){
                var optEle = $(this).find('.form-control');
                $(this).siblings('.opt-wrap').each(function(){
                    if(optEle.val() == $(this).find('.form-control').val()){
                        $(this).find('.form-control').focus()
                        $(this).find('.form-control').siblings('.duplicate-message').text('Trùng tên phân loại hàng').css('color','red')
                        stop=true;
                        return !stop;
                    }
                })
                return !stop;
            })
            return !stop;
        })
        if(stop) return ;
        if(nameArr.length !== new Set(nameArr).size){
            $('.var-box').first().find('.var-name-input')
                .siblings('.duplicate-message').text('Tên nhóm phải khác nhau').css('color', 'red');
            $('.var-box').first().find('.var-name-input').focus();
        }
        else {
            $('#create-product-form').submit();
        }
    }
})


$('.thumbnail-add-btn').on('click', function(){
    $('#thumbnail-add-btn').click();
})
function addThumbnail(event){
    if(event.target.files.length != 0){
        var tmppath = URL.createObjectURL(event.target.files[0]);
        var file = event.target.files[0];
        if(!checkImgInput(file)){
            $('#thumbnail-add-btn').val('');
            alert(file['name']+" không thể được tải lên.\n "+"Định dạng file không hợp lệ (chỉ hỗ trợ PNG, JPG, JPEG)")
        }else{
            //remove added thumbnail
            $('#thumbnail-add-btn').siblings('.img-wrap').remove();
            //create thumbnail preview
            var img = $("<img />");
            img.attr('src', tmppath);
            var img_wrap = "<div class='img-wrap'>"+($('<div>').append(img)).html()+"<div class='img-action'><i class='fa-regular fa-trash-can' onclick='del_thumbnail(this)'></i></div></div>"
            $(img_wrap).append(img);
            $('#thumbnail-add-btn').parent().append(img_wrap);
            $('.thumbnail-add-btn').css('display','none');
        }
    }
}
function del_thumbnail(element){
    $(element).parent().parent().remove();
    $('#thumbnail-add-btn').val('');
    $('.thumbnail-add-btn').css('display','block');
}
//clone visible input value and set to hidden input
function alter_input(element){
    var temp1 = $(element).val();
    var temp2 = $(element).siblings('select').val();
    var input_value ;
    if(temp2 == null){
        temp2 = $(element).siblings('input.form-control').val();
        input_value = temp2 + temp1;
    }else
        input_value = temp1 + temp2;
    $(element).siblings('input[type=hidden]').val(input_value);
}
$('.default-opt').prepend($("<option></option>").text('-- Vui lòng chọn --').attr('selected','selected').attr('disabled','disabled').val(''))

//trigger hidden input file
$('.img-add-btn').on('click', function(){
    $("#add-image-btn").click();
})
function checkImgInput(file){
    const fileType = file['type'];
    const validImageTypes = ['image/jpeg', 'image/png', 'image/jpg'];
    if (!validImageTypes.includes(fileType)) {
        return false;
    }
    return true;
}
function addOptionToVar(ele){
    var var_opt_count = $(ele).siblings('.var-opt-counter').val();
    var var_index = $(ele).siblings('.var-index').val();
    if(var_opt_count < 4){
        var opt_input = "<div class='opt-wrap'>"
            +"<span>"
                +"<input class='form-control' name='variantDTO["+var_index+"].valueDTO["+var_opt_count+"].name'"
                    +"type='text' placeholder='ví dụ: đỏ v.v' required onchange='checkDuplicateVariantValue(this);this.oldvalue = this.value;' onfocus='this.oldvalue'>"
                +"<span class='duplicate-message'></span>"
                +"<span class='validate-message'></span>"
            +"</span>"
            +"<div onclick='delInputVarOpt(this)'><i class='fa-regular fa-trash-can fa-lg'></i></div></div>";
        $(ele).parent().parent().siblings(".form-line").children(".var-wrap").append(opt_input);
        var_opt_count = +var_opt_count + +1;
        $(ele).siblings('.var-opt-counter').val(var_opt_count)
        $(ele).children('span').text('Thêm phân loại hàng('+var_opt_count+'/4)')
    }
    if(var_opt_count == 4)
        $(ele).css('cursor',"not-allowed")
}
function delInputVarOpt(element){
    var var_opt_count = $(element).parents().parent().parent().siblings('.form-line').children().children('.var-opt-counter');
    if(var_opt_count.val() > 1){
        $(element).parent().remove();
        var_opt_count.val(+var_opt_count.val() - +1) ;
        var_opt_count.siblings('.opt-add-btn').children('span').text('Thêm phân loại hàng('+var_opt_count.val()+'/4)');
    }
    if(var_opt_count.val() < 4)
        var_opt_count.siblings('.opt-add-btn').css('cursor',"pointer");  
}
function createVarInput(element){
    var var_index = $('#variant-counter').val();
    $(element).parent().parent().parent().append(
                "<div class='var-box'>"
                    +"<div class='form-line'>"
                        +"<div style='display: flex;'>"
                            +"<span class='label'>Nhóm phân loại "+(+var_index + +1)+"</span>"
                            +"<span>"
                                +"<input class='form-control var-name-input' name='variantDTO["+var_index+"].name'  type='text' required"
                                    +" placeholder='ví dụ: màu sắc v.v' onchange='checkDuplicateVariantName(this)'>"
                                +"<span class='duplicate-message'></span>"
                                +"<span class='validate-message'></span>"
                            +"</span>"
                            +"<input class='var-opt-counter' type='hidden' value='1'>"
                            +"<input class='var-index' type='hidden' value='"+var_index+"'>"
                            +"<div class='opt-add-btn' onclick='addOptionToVar(this)'>"
                                +"<i class='fa-solid fa-plus'></i>"
                                +"<span>Thêm phân loại hàng(1/4)</span>"
                            +"</div>"
                        +"</div>"
                    +"</div>"
                    +"<div class='form-line'>"
                        +"<span class='label'>Phân loại hàng</span>"
                        +"<div class='var-wrap'>"
                            +"<div class='opt-wrap'>"
                                +"<span>"
                                    +"<input class='form-control' name='variantDTO["+var_index+"].valueDTO[0].name'  type='text' required"
                                        +" placeholder='ví dụ: đỏ v.v'  onchange='checkDuplicateVariantValue(this);this.oldvalue = this.value;' onfocus='this.oldvalue'>"
                                    +"<span class='duplicate-message'></span>"
                                    +"<span class='validate-message'></span>"
                                +"</span>"
                                +"<div onclick='delInputVarOpt(this)'>"
                                    +"<i class='fa-regular fa-trash-can fa-lg'></i>"
                                +"</div>"
                            +"</div>"
                        +"</div>"
                    +"</div>"
                +"</div>");
    $('#variant-counter').val(+var_index+ +1);
    if($('#variant-counter').val() < 2)
        $(element).parent().parent().parent().append($(element).parent().parent().clone());
    $(element).parent().parent().remove();
    
    if($('#no-variant-input-box').length){
        localStorage.setItem("no-variant-input-box", $('<div>').append($('#no-variant-input-box').clone()).html());
        $('#no-variant-input-box').remove();
    }
}
//check duplicate variant name
function checkDuplicateVariantName(element){
    if($(element).val().trim().length == 0){
        $(element).siblings('.validate-message').text('Tên nhóm không hợp lệ').css('color','red');
    }else{
        $(element).siblings('.duplicate-message').text('');
        if($('.var-name-input').length == 2){
            var name1 = $('.var-name-input').eq(0).val();
            var name2 = $('.var-name-input').eq(1).val();
            if(name1 == name2 && name1 != '' && name2 != ''&& name1 != ' ' && name2 != ' '){
                $(element).siblings('.duplicate-message').text('Tên nhóm phải khác nhau').css('color','red');
            }else{
                $('.var-name-input').siblings('.duplicate-message').text('')
            }
        }
    }
        
}

// check duplicate variant value
function checkDuplicateVariantValue(element){
    var siblings = $(element).parent().parent().siblings('.opt-wrap');
    var check = false; //check if value already exist
    var newValue = element.value;
    var oldValue = element.oldvalue;
    $(siblings).each(function(){
        var siblingValue = $(this).children().children('input').val()
        if(oldValue == siblingValue){
            oldValue='';
            $(this).children().children('input').siblings('span .duplicate-message').text('');
        }
        if(newValue == siblingValue && newValue != ''){
            $(element).siblings('span .duplicate-message').text('Trùng tên phân loại hàng').css('color','red');
            check=true;
        }
        if(!check){
            $(element).siblings('span .duplicate-message').text('');
        }
        
    })
    return check;
}

//validate form 
if($('#create-product-form').length >= 1)
$('#create-product-form').validate({
    errorPlacement: function(error, element) {
        $(element).siblings(".validate-message").text(error[0].innerHTML).css('color','red');
        $(element).parent().siblings(".validate-message").text(error[0].innerHTML).css('color','red');
    },
    success: function(element){
        $(element).siblings(".validate-message").text('')
        $(element).parent().siblings(".validate-message").text('');
    },
    submitHandler: function (form) {
        console.log('check submit handle')
        form.submit();
    }
})
var img_count = 0;
var img_index = 0;
//select img file and show image preview
//check file extension
$("#add-image-btn").change(function addImage(event){
    var tmppath = URL.createObjectURL(event.target.files[0]);
    var file = event.target.files[0];
    if(!checkImgInput(file)){
        alert(file['name']+" không thể được tải lên.\n "+"Định dạng file không hợp lệ (chỉ hỗ trợ PNG, JPG, JPEG)")
    }else{
        $clone = $(this).clone();
        var img = $("<img />");
        var img_wrap = "<div class='img-wrap'><div class='img-action'><i class='fa-regular fa-trash-can' onclick='del_img(this)'></i></div></div>"
        img.attr('src', tmppath)
        $clone.attr('name', 'imageDTO['+img_index+'].file').removeClass().removeAttr('id');
        $('#img-box').append($(img_wrap).append($clone).append(img));
        img_index += +1;
        img_count += +1;
        $(this).val('')
        $('.img-add-btn span').text('Thêm hình ảnh ('+img_count+'/9)');
        if(img_count == 9){
            $('.img-add-btn').css('cursor', 'not-allowed');
            $('.img-add-btn').off('click');
        }
    }
});
//delete image
function del_img(ele){
    $(ele).parent().parent().remove();
    img_count -= +1;
    $('.img-add-btn span').text('Thêm hình ảnh ('+img_count+'/9)')
    if(img_count + +1 == 9 ){
        $('.img-add-btn').css('cursor', 'pointer');
        $('.img-add-btn').on('click', function(){
            $("#add-image-btn").click();
        });
    }
}
//product-detail-admin.html
//validate
if($('#update-product-form').length >=1)
    jQuery.validator.addMethod("greaterThan1000", function(value, element) {
        var tempPrice = value.replaceAll(',','').replaceAll('.','');
        if(+tempPrice >= 1000) return true;
        else return false;
    }, "Giá bán tối thiểu là 1000");
var rules = new Object();
$('input.temp_price').each(function(){
    rules[this.name] = { greaterThan1000: true };
})
if($('#update-product-form').length >=1)
    $('#update-product-form').validate({
        rules: rules,
        errorPlacement: function(error, element) {
            $(element).parent().siblings(".validate-message").text(error[0].innerHTML).css('color','red');
            $(element).siblings(".validate-message").text(error[0].innerHTML).css('color','red');
        },
        success: function(element){
            $(element).parent().siblings(".validate-message").text('');
            $(element).siblings(".validate-message").text('')
        },
        submitHandler: function (form) {
            form.submit();
        }
    })

function mergeTableRow(element, next, length, rowspan, column) {
    if (next >= length - +1) return;
    var $this = $(element).children('td').eq(column);
    var $next = $(element).siblings().eq(next).children('td').eq(column);
    if ($this.text() == $next.text()) {
        $this.attr('rowspan', +rowspan + +1);
        $next.css('display', 'none');
        mergeTableRow(element, next += +1, length, +rowspan + +1, column);
    } else {
        mergeTableRow($(element).siblings().eq(next), next += +1, length, 1, column);
    }
}
$('input.temp_price, input.price-range-input').keydown(function (evt) {
    if ((/^[0-9]*\.?[0-9]*$/).test(evt.key)) {
        return true;
    } else if (evt.key == 'Backspace' || evt.key == 'Enter')
        return true;
    else return false;
});
$('input.temp_price').keyup(function () {
    var price = $(this).val().replaceAll(',', '').replaceAll('.', '');
    $(this).siblings('.hidden_price').val(price)
    $(this).val(numberWithCommas(+price));
})

//product-list.html
$('input.price-range-input').on('keyup',function(){
    var price = $(this).val().replaceAll(',', '').replaceAll('.', '');
    $(this).val(numberWithCommas(+price));
    var min = $('input.price-range-input').eq(0).val().replaceAll(',', '').replaceAll('.', '');
    var max = $('input.price-range-input').eq(1).val().replaceAll(',', '').replaceAll('.', '');
    createPriceRange(min, max);
})

function createPriceRange(min, max){
    if(min.length == 0 || min == null || isNaN(min) || min < 0) min=0;
    if(max.length == 0 || max == null || isNaN(max) || max <= 0 ) max = 999999999;
    $('#hidden-price-range-input').val(min+'-'+max);
    $('#hidden-price-range-input').siblings().children(".check-box__label").text(intToString(min)+' - '+intToString(max));
    //$('#hidden-price-range-checkbox').attr('hidden', false)
}
const intToString = num => {
    num = num.toString().replace(/[^0-9.]/g, '');
    if (num < 1000) {
        return num;
    }
    let si = [
        {v: 1E3, s: "ngàn"},
        {v: 1E6, s: "triệu"},
        {v: 1E9, s: "tỷ"},
        {v: 1E12, s: "nghìn tỷ"},
        {v: 1E15, s: "triệu tỷ"},
        {v: 1E18, s: "tỷ tỷ"}
    ];
    let index;
    for (index = si.length - 1; index > 0; index--) {
        if (num >= si[index].v) {
            break;
        }
    }
    return (num / si[index].v).toFixed(2).replace(/\.0+$|(\.[0-9]*[1-9])0+$/, "$1") + si[index].s;
};

//account-detail.html
function matchPassword(){
    var psw1 = document.getElementById('password').value;
    var psw2 = document.getElementById('confirm_password').value;
    if(psw1 != psw2){
        console.log("ko khop");
        document.getElementById('confirmPassLabel').style.visibility="visible";
        document.getElementById("confirm_password").focus();
        return false;
    }else return true;
}
if($('#update-account-detail-form').length == 1){
    $.validator.messages.required = 'Không được để trống';
    $('#update-account-detail-form').validate({
        messages:{
            name:{
                pattern: "Tên không chứa chữ số hoặc ký tự đặc biệt"
            },
            phone:{
                pattern: "Số điện thoại có dạng 10 chữ số từ 0-9"
            }
        },
        errorPlacement: function(error, element) {
            $(element).siblings(".validate-message").text(error[0].innerHTML);
        },
        success: function(element){
            $(element).siblings(".validate-message").text('')
        },
        submitHandler: function (form) {
            form.submit();
        }
    })
}

//order-detail-admin.html
function checkOrderQuantity(){
    if($('.quantity-invalid').length > 0){
        $('.modal-body').text('');
        var count = $('.quantity-invalid').length;
        $('.modal-body').append(count + " Sản phẩm không đủ số lượng để xác nhận đơn hàng: ")
        var list = $("<ul></ul>");
        $('.quantity-invalid').each(function(index, elem){
            var product_name = $(elem).parent().siblings().eq(0).find(".table-p__name").find('a').text();
            $(list).append($("<li>"+product_name+"</li>"))
        })
        $('.modal-body').append(list)
        $('.modal-body').append($("<span>Vui lòng cập nhật số lượng sản phẩm hoặc hủy đơn hàng</span>"))
        $('#order-accept-submit-btn').attr('hidden',true);
        $("#modal-not-enough-quantity").modal('show');
    }else if($('#isAccountEnable').val() == 0){
        $('.modal-body').text('Tài khoản này đã bị khóa, bạn vẫn muốn xác nhận đơn hàng?').css({'color':'red','font-weight':'600'})
        $('#order-accept-submit-btn').attr('hidden',false).unbind('click')
        $('#order-accept-submit-btn').on('click',function(){$('#form-order-accept').submit()})
        $("#modal-not-enough-quantity").modal('show');
    }else if($('.quantity-invalid').length == 0){
        $('.modal-body').text('Xác nhận đơn đặt hàng này?')
        $('#order-accept-submit-btn').attr('hidden',false).unbind('click')
        $('#order-accept-submit-btn').on('click',function(){$('#form-order-accept').submit()})
        $("#modal-not-enough-quantity").modal('show');
        //$('#form-order-accept').submit()
    }else console.log("error sending accept order form")
}

function confirmRejectOrder(){
    $('.modal-body').text('').append($("<div>Xác nhận hủy đơn hàng?</div>").css({'color':'red','font-weight':'600'}))
    $('#order-accept-submit-btn').attr('hidden',false).unbind('click')
    $('#order-accept-submit-btn').on('click',function(){$('#form-reject-order').submit()})
    $("#modal-not-enough-quantity").modal('show');
}

function confirmShippingOrder(){
    $('.modal-body').text('Xác nhận đang giao hàng?')
    $('#order-accept-submit-btn').attr('hidden',false).unbind('click')
    $('#order-accept-submit-btn').on('click',function(){$('#form-order-shipping').submit()})
    $("#modal-not-enough-quantity").modal('show');
}

function confirmShippedOrder(){
    $('.modal-body').text('Xác nhận đơn hàng giao thành công?')
    $('#order-accept-submit-btn').attr('hidden',false).unbind('click')
    $('#order-accept-submit-btn').on('click',function(){$('#form-order-shipped').submit()})
    $("#modal-not-enough-quantity").modal('show');
}

function confirmOrderFailed(){
    $('.modal-body').text('Xác nhận giao hàng thất bại?')
    $('#order-accept-submit-btn').attr('hidden',false).unbind('click')
    $('#order-accept-submit-btn').on('click',function(){$('#form-order-failed').submit()})
    $("#modal-not-enough-quantity").modal('show');
}

function printOrder(orderStatus){
    $('iframe').remove();
    var returnUrl = "https://dev-online-gateway.ghn.vn/a5/public-api/printA5?token=";
    var url ="https://dev-online-gateway.ghn.vn/shiip/public-api/v2/a5/gen-token";
    $.ajax({
        url :  url,
        type: 'get',
        headers :{
            token: "201937cd-db8b-11ed-ab31-3eeb4194879e"
        },
        data :{
            order_codes: orderStatus
        },
        success: function(res) {
            var printToken = res.data.token ;
            printUrl = returnUrl+printToken;
            $("<iframe></iframe>").hide().attr("src", printUrl).appendTo("body");
        },
        error: function(xhr,status) {
            console.log(xhr);
            console.log(status);
        }
    })
}
